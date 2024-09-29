package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResRoleDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role handleCreateRole(Role role) {
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }

        return this.roleRepository.save(role);
    }

    public void handleDeleteRole(long id) {
        // delete job (inside job_role table)
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        Role currentRole = roleOptional.get();
        // delete role
        this.roleRepository.deleteById(id);
    }

    public Role fetchRoleById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllRole(
            Specification<Role> spec, Pageable pageable
    // Pageable pageable
    ) {
        Page<Role> pageRole = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageRole.getTotalPages());
        mt.setTotal(pageRole.getTotalElements());
        rs.setMeta(mt);
        List<ResRoleDTO> listRole = pageRole.getContent()
                .stream().map(item -> new ResRoleDTO(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getActive(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getCreatedBy(),
                        item.getUpdatedBy(),
                        item.getPermissions()))
                .collect(Collectors.toList());
        rs.setResult(listRole);
        return rs;
    }

    public Role handleUpdateRole(Role reqRole) {
        Optional<Role> roleOptional = this.roleRepository.findById(reqRole.getId());
        if (roleOptional.isPresent()) {
            Role currentRole = roleOptional.get();
            currentRole.setName(reqRole.getName());
            currentRole.setDescription(reqRole.getDescription());
            currentRole.setActive(reqRole.getActive());
            if (reqRole.getPermissions() != null) {
                List<Long> reqPermissions = reqRole.getPermissions()
                        .stream().map(x -> x.getId())
                        .collect(Collectors.toList());
                List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
                currentRole.setPermissions(dbPermissions);
            }

            // update
            currentRole = this.roleRepository.save(currentRole);
            return currentRole;
        } else {
            return null;
        }
    }

    public boolean isNameExist(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role handleGetRoleById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        }
        return null;
    }

}
