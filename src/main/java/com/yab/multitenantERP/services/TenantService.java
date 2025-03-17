package com.yab.multitenantERP.services;

import com.yab.multitenantERP.entity.Tenant;
import com.yab.multitenantERP.repositories.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public Tenant registerTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    public List<Tenant> getTenants(){
        return tenantRepository.findAll();
    }
}
