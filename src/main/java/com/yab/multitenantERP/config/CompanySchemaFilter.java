package com.yab.multitenantERP.config;

import com.yab.multitenantERP.entity.Tenant;
import com.yab.multitenantERP.repositories.TenantRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class CompanySchemaFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CompanySchemaFilter.class);

    private final TenantRepository tenantRepository;

    public CompanySchemaFilter(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }
    private static final List<String> PERMITTED_PATHS = List.of(
            "/tenant/getTenants"
    );
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        if (PERMITTED_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String companySchema = request.getHeader("X-Company-Schema");


        if (companySchema == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tenant schema header (X-Company-Schema) is required.");
        }

        try {
            Long tenantId = Long.parseLong(companySchema);
            Optional<Tenant> tenant = tenantRepository.findById(tenantId);

            if (tenant.isPresent()) {
                CompanyContextHolder.setCompanySchema(tenant.get().getName());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tenant with ID " + tenantId + " not found.");
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid tenant ID format: " + companySchema);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            CompanyContextHolder.clear();
        }
    }
}