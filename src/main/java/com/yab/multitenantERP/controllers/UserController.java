package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.UserEntity;
import com.yab.multitenantERP.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping
    public List<UserEntity> getUsers(@RequestHeader("X-Company-Schema") String companyName){
        return userService.getUsers();
    }
}