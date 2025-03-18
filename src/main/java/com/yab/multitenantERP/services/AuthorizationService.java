package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.ApiEntity;
import com.yab.multitenantERP.entity.Permission;
import com.yab.multitenantERP.entity.UserEntity;
import com.yab.multitenantERP.repositories.ApiEndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService {

    @Autowired
    private ApiEndpointRepository apiRepository;

    public boolean hasAccess(UserEntity user, String path, String method) {
        Optional<ApiEntity> apiOpt = apiRepository.findByPathAndMethod(path, method);

        if (apiOpt.isEmpty()) {
            return false;  // API not registered
        }

        ApiEntity api = apiOpt.get();

        // Check if user has direct permission
        for (Permission permission : user.getPermissions()) {
            if (permission.getName().equalsIgnoreCase(api.getPath() + ":" + api.getMethod())) {
                return true;
            }
        }

        // Check role-based permissions
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.getName().equalsIgnoreCase(api.getPath() + ":" + api.getMethod()));
    }
}
