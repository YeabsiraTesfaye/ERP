package com.yab.multitenantERP.services;

import com.yab.multitenantERP.config.CompanyContextHolder;
import com.yab.multitenantERP.entity.Permission;
import com.yab.multitenantERP.entity.Role;
import com.yab.multitenantERP.entity.Tenant;
import com.yab.multitenantERP.entity.UserEntity;
import com.yab.multitenantERP.repositories.PermissionRepository;
import com.yab.multitenantERP.repositories.RoleRepository;
import com.yab.multitenantERP.repositories.TenantRepository;
import com.yab.multitenantERP.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;
    private final PermissionRepository permissionRepository;


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       PermissionRepository permissionRepository,
                       TenantRepository tenantRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.tenantRepository = tenantRepository;
        this.permissionRepository = permissionRepository;
    }

    public UserEntity registerUser(UserEntity user) {
        return userRepository.save(user);
    }

    public List<UserEntity> getUsers(){
        return userRepository.findAll();
    }

    @PostConstruct
    public void createDefaultUser() {
        CompanyContextHolder.setCompanySchema("public");
        if (userRepository.count() == 0) {
            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role newRole = new Role("ROLE_ADMIN");
                        return roleRepository.save(newRole); // Save the new role before associating with user
                    });

            Tenant tenant = tenantRepository.findById(1L)
                    .orElseGet(() -> {
                        Tenant newTenant = new Tenant("public");
                        return  tenantRepository.save(newTenant);

                            });



            UserEntity user = new UserEntity();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin123"));

            userRepository.save(user);
            System.out.println("Default admin user created!");
        }
    }

    public Optional<UserEntity> getUserByUsername(String username){
        return  userRepository.findByUsername(username);
    }
    public Set<Role> getUserRoles(String username) {
        // Fetch the user by username
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return the roles associated with the user
        return user.getRoles(); // This will return a Set<Role>
    }

    public void grantPermissionToUser(Long userId, Long permissionId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        user.getPermissions().add(permission);
        userRepository.save(user);
    }

    public void assignRoleToUser(Long userId, Long roleId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userRepository.save(user);
    }


}
