package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.services.RoleService;
import com.yab.multitenantERP.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    private final RoleService roleService;

    AdminController(UserService userService, RoleService roleService){
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/grant-permission-to-user")
    public ResponseEntity<String> grantPermissionToUser(
            @RequestBody Long userId, @RequestParam Long permissionId) {
        userService.grantPermissionToUser(userId, permissionId);
        return ResponseEntity.ok("Permission granted to user");
    }

    @PostMapping("/grant-permission-to-role")
    public ResponseEntity<String> grantPermissionToRole(
            @RequestBody Long roleId, @RequestBody Long permissionId) {
        roleService.grantPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok("Permission granted to role");
    }

    @PostMapping("/assign-role-to-user")
    public ResponseEntity<String> assignRoleToUser(
            @RequestBody Long userId, @RequestBody Long roleId) {
        userService.assignRoleToUser(userId, roleId);
        return ResponseEntity.ok("Role assigned to user");
    }
}
