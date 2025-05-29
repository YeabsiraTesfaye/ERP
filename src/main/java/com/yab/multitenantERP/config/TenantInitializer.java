package com.yab.multitenantERP.config;

import com.yab.multitenantERP.entity.Tenant;
import com.yab.multitenantERP.repositories.TenantRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TenantInitializer implements ApplicationRunner {

    private final TenantRepository tenantRepository;
    private final DynamicSchemaRoutingDataSource routingDataSource;

    public TenantInitializer(TenantRepository tenantRepository, DynamicSchemaRoutingDataSource routingDataSource) {
        this.tenantRepository = tenantRepository;
        this.routingDataSource = routingDataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<Object, Object> targetDataSources = new HashMap<>();
        List<Tenant> tenants = tenantRepository.findAll();
        tenants.forEach(tenant -> {
            DataSource ds = DataSourceBuilder.create()
                    .url("jdbc:postgresql://localhost:5432/erp?currentSchema=" + tenant.getName())
                    .username("postgres")
                    .password("root")
                    .driverClassName("org.postgresql.Driver")
                    .build();
            targetDataSources.put(tenant.getName(), ds);
        });
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.afterPropertiesSet();
    }
}
