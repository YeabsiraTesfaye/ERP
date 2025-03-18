package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Permission;
import com.yab.multitenantERP.entity.Role;
import com.yab.multitenantERP.repositories.PermissionRepository;
import com.yab.multitenantERP.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;

    RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository){
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }
    public void grantPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        role.getPermissions().add(permission);
        roleRepository.save(role);
    }
}
