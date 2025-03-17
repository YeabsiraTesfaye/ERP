package com.yab.multitenantERP.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicSchemaRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return CompanyContextHolder.getCompanySchema();
    }
}