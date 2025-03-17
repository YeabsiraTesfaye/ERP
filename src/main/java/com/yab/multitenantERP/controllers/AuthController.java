package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.JwtUtil;
import com.yab.multitenantERP.dtos.LoginRequest;
import com.yab.multitenantERP.dtos.LoginResponse;
import com.yab.multitenantERP.dtos.SignupRequest;
import com.yab.multitenantERP.dtos.UserDTO;
import com.yab.multitenantERP.entity.Role;
import com.yab.multitenantERP.entity.UserEntity;
import com.yab.multitenantERP.repositories.RoleRepository;
import com.yab.multitenantERP.repositories.UserRepository;
import com.yab.multitenantERP.services.UserService;
import com.yab.multitenantERP.utils.UserMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    public AuthController(UserService userService, UserRepository userRepository, JwtUtil jwtUtil, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
        this.userService = userService;

    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestHeader("X-Company-Schema") String companySchema, @Valid @RequestBody SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setTenant_id(request.getTenant_id());
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }

        UserEntity user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUsername());

        // Map User entity to UserDTO using UserMapper
        UserDTO userDTO = UserMapper.getUserByUsername(request.getUsername(), userRepository);

        Set<Role> roles = userService.getUserRoles(request.getUsername());
        userDTO.setRoles(roles);


        // Create response with token and user details
        LoginResponse response = new LoginResponse(
                token,
                userDTO // Pass the whole UserDTO here
        );

        return ResponseEntity.ok(response);
    }
}
