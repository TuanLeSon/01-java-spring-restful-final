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
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    public final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a new permission")
    public ResponseEntity<Permission> createNewPermission(@Valid @RequestBody Permission reqPermission)
            throws IdInvalidException {
        if (this.permissionService.isNameExist(reqPermission.getName())) {
            throw new IdInvalidException(
                    "Permission " + reqPermission.getName() + " đã tồn tại, vui lòng sử dụng permission khác");
        }

        if (this.permissionService.isPermissionExist(reqPermission)) {
            throw new IdInvalidException(
                    "Permission " + reqPermission.getName() + " đã tồn tại, vui lòng sử dụng permission khác");
        }

        Permission permission = this.permissionService.handleCreatePermission(reqPermission);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") long id) throws IdInvalidException {
        if (permissionService.fetchPermissionById(id) == null) {
            throw new IdInvalidException("Permission với id = " + id + " không tồn tại");
        }
        this.permissionService.handleDeletePermission(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/permissions/{id}")
    @ApiMessage("fetch permission by id")
    public ResponseEntity<Permission> getPermissionById(@PathVariable("id") long id) throws IdInvalidException {
        if (permissionService.fetchPermissionById(id) == null) {
            throw new IdInvalidException("Permission với id = " + id + " không tồn tại");
        }
        Permission permission = this.permissionService.fetchPermissionById(id);
        return ResponseEntity.ok(permission);
    }

    @GetMapping("/permissions")
    @ApiMessage("fetch all permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermission(
            @Filter Specification<Permission> spec,
            Pageable pageable) {
        ResultPaginationDTO permissions = this.permissionService.fetchAllPermission(spec, pageable);
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission reqPermission)
            throws IdInvalidException {

        Permission currentPermission = this.permissionService.fetchPermissionById(reqPermission.getId());
        // check id
        if (currentPermission == null) {
            throw new IdInvalidException("Permission với id = " + reqPermission.getId() + " không tồn tại");
        }
        // check name
        if (currentPermission != null && this.permissionService.isNameExist(currentPermission.getName())
                && !(currentPermission.getName()).equals(reqPermission.getName())) {
            throw new IdInvalidException("Permission với name = " + reqPermission.getName() + " đã tồn tại");
        }
        if (this.permissionService.isPermissionExist(reqPermission)) {
            if (this.permissionService.isNameExist(currentPermission.getName())) {
                throw new IdInvalidException("Permission đã tồn tại");
            }
        }
        Permission permission = this.permissionService.handleUpdatePermission(reqPermission);
        return ResponseEntity.ok(permission);
    }
}
