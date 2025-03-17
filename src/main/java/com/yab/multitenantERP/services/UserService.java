package com.yab.multitenantERP.services;

import com.yab.multitenantERP.config.CompanyContextHolder;
import com.yab.multitenantERP.entity.Role;
import com.yab.multitenantERP.entity.Tenant;
import com.yab.multitenantERP.entity.UserEntity;
import com.yab.multitenantERP.repositories.RoleRepository;
import com.yab.multitenantERP.repositories.TenantRepository;
import com.yab.multitenantERP.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository,
                       TenantRepository tenantRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.tenantRepository = tenantRepository;
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
            user.setTenant_id(tenant.getId());
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRoles(Set.of(roleAdmin)); // Assign role(s)

            userRepository.save(user);
            System.out.println("Default admin user created!");
        }
    }
    public Set<Role> getUserRoles(String username) {
        // Fetch the user by username
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return the roles associated with the user
        return user.getRoles(); // This will return a Set<Role>
    }
}
