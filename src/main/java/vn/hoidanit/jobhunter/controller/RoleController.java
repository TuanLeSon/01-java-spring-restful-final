package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    public final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a new role")
    public ResponseEntity<Role> createNewRole(@Valid @RequestBody Role reqRole) throws IdInvalidException {
        if (roleService.isNameExist(reqRole.getName())) {
            throw new IdInvalidException("Role " + reqRole.getName() + " đã tồn tại, vui lòng sử dụng role khác");
        }
        Role role = this.roleService.handleCreateRole(reqRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") long id) throws IdInvalidException {
        if (roleService.fetchRoleById(id) == null) {
            throw new IdInvalidException("Role với id = " + id + " không tồn tại");
        }
        this.roleService.handleDeleteRole(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("fetch role by id")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") long id) throws IdInvalidException {
        if (roleService.fetchRoleById(id) == null) {
            throw new IdInvalidException("Role với id = " + id + " không tồn tại");
        }
        Role role = this.roleService.fetchRoleById(id);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/roles")
    @ApiMessage("fetch all roles")
    public ResponseEntity<ResultPaginationDTO> getAllRole(
            @Filter Specification<Role> spec,
            Pageable pageable

    ) {

        ResultPaginationDTO roles = this.roleService.fetchAllRole(spec, pageable);
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@RequestBody Role reqRole) throws IdInvalidException {

        Role currentRole = this.roleService.fetchRoleById(reqRole.getId());
        // check id
        if (currentRole == null) {
            throw new IdInvalidException("Role với id = " + reqRole.getId() + " không tồn tại");
        }
        // check name
        if (currentRole != null && this.roleService.isNameExist(currentRole.getName())
                && !(currentRole.getName()).equals(reqRole.getName())) {
            throw new IdInvalidException("Role với name = " + reqRole.getName() + " đã tồn tại");
        }

        Role role = this.roleService.handleUpdateRole(reqRole);
        return ResponseEntity.ok(role);
    }
}
