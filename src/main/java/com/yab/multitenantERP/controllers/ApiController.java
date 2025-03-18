package com.yab.multitenantERP.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/manage-users")
    @PreAuthorize("hasPermission('api/manage-users', 'GET')")
    public String manageUsers() {
        return "You have access to manage users.";
    }

    @PostMapping("/create-report")
    @PreAuthorize("hasPermission('api/create-report', 'POST')")
    public String createReport() {
        return "You have access to create a report.";
    }
}
