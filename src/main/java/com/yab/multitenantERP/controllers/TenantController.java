package com.yab.multitenantERP.controllers;

import com.yab.multitenantERP.entity.Tenant;
import com.yab.multitenantERP.entity.UserEntity;
import com.yab.multitenantERP.services.SchemaInitializer;
import com.yab.multitenantERP.services.TenantService;
import com.yab.multitenantERP.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenant")
public class TenantController {

    private final SchemaInitializer schemaInitializer;
    TenantController(SchemaInitializer schemaInitializer){
        this.schemaInitializer = schemaInitializer;
    }
    @PostMapping
    public String createUser(@RequestHeader("X-Company-Schema") String companySchema,
                             @RequestBody Tenant tenant) {
        schemaInitializer.createSchema(tenant.getName());
        return "Tenant created: ";
    }

    @GetMapping("/getTenants")
    public List<Tenant> getTenants(){
        return schemaInitializer.tenants();
    }


    @GetMapping("/getTenant/{id}")
    public Tenant getTenant(@PathVariable Long id){
        return schemaInitializer.getTenant(id);
    }
}