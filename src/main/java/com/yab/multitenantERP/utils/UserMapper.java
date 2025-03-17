package com.yab.multitenantERP.utils;


import com.yab.multitenantERP.dtos.UserDTO;
import com.yab.multitenantERP.entity.UserEntity;
import com.yab.multitenantERP.repositories.UserRepository;

import java.util.stream.Collectors;

public class UserMapper {
    // Existing toDTO method

    public static UserDTO toDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setTenant_id(user.getTenant_id());
//
//        userDTO.setRoles(
//                user.getRoles().stream()
//                        .map(role -> new RoleDTO(role.getId(), role.getName()))
//                        .collect(Collectors.toSet())
//        );
        return userDTO;
    }

    // Add this method to fetch UserDTO by Username
    public static UserDTO getUserByUsername(String username, UserRepository userRepository) {
        return userRepository.findByUsername(username)
                .map(UserMapper::toDTO)
                .orElse(null); // Return null if user not found
    }
}
