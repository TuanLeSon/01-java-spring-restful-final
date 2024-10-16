package vn.javaproject.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.javaproject.jobhunter.domain.Permission;
import vn.javaproject.jobhunter.domain.response.ResPermissionDTO;
import vn.javaproject.jobhunter.domain.response.ResultPaginationDTO;
import vn.javaproject.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission handleCreatePermission(Permission permission) {
        return this.permissionRepository.save(permission);
    }

    public void handleDeletePermission(long id) {
        // delete job (inside job_permission table)
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(job -> job.getPermissions().remove(currentPermission));
        // delete permission
        this.permissionRepository.deleteById(id);
    }

    public Permission fetchPermissionById(long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            return permissionOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllPermission(
            Specification<Permission> spec, Pageable pageable
    // Pageable pageable
    ) {
        Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pagePermission.getTotalPages());
        mt.setTotal(pagePermission.getTotalElements());
        rs.setMeta(mt);
        List<ResPermissionDTO> listPermission = pagePermission.getContent()
                .stream().map(item -> new ResPermissionDTO(
                        item.getId(),
                        item.getName(),
                        item.getApiPath(),
                        item.getMethod(),
                        item.getModule(),
                        item.getUpdatedAt(),
                        item.getCreatedAt(),
                        item.getCreatedBy(),
                        item.getUpdatedBy()))
                .collect(Collectors.toList());
        rs.setResult(listPermission);
        return rs;
    }

    public Permission handleUpdatePermission(Permission reqPermission) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(reqPermission.getId());
        if (permissionOptional.isPresent()) {
            Permission currentPermission = permissionOptional.get();
            currentPermission.setName(reqPermission.getName());
            currentPermission.setApiPath(reqPermission.getApiPath());
            currentPermission.setMethod(reqPermission.getMethod());
            currentPermission.setModule(reqPermission.getModule());
            // update
            currentPermission = this.permissionRepository.save(currentPermission);
            return currentPermission;
        } else {
            return null;
        }
    }

    public boolean isNameExist(String name) {
        return this.permissionRepository.existsByName(name);
    }

    public Permission handleGetPermissionById(long id) {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            return permissionOptional.get();
        }
        return null;
    }

    public Boolean isPermissionExist(Permission permission) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(),
                permission.getApiPath(),
                permission.getMethod());
    }

}
