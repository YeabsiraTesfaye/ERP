package com.yab.multitenantERP.config;

import com.yab.multitenantERP.JwtUtil;
import com.yab.multitenantERP.entity.Tenant;
import com.yab.multitenantERP.repositories.TenantRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CompanySchemaFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CompanySchemaFilter.class);

    private final TenantRepository tenantRepository;
    private final JwtUtil jwtUtil;

    public CompanySchemaFilter(TenantRepository tenantRepository, JwtUtil jwtUtil) {
        this.tenantRepository = tenantRepository;
        this.jwtUtil = jwtUtil;
    }

    // Paths that do not require tenant schema validation and token checking
    private static final List<String> PERMITTED_PATHS = List.of(
            "/tenant/getTenants"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Skip processing for preflight OPTIONS requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        if (PERMITTED_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract tenant schema from header
        String companySchema = request.getHeader("X-Company-Schema");
        if (companySchema == null || companySchema.trim().isEmpty()) {
            logger.warn("Missing X-Company-Schema header");
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Tenant schema header (X-Company-Schema) is required.");
            return;
        }
        try {
            Long tenantId = Long.parseLong(companySchema);
            Optional<Tenant> tenant = tenantRepository.findById(tenantId);
            if (tenant.isPresent()) {
                String schemaName = tenant.get().getName();
                CompanyContextHolder.setCompanySchema(schemaName);
            } else {
                response.sendError(HttpStatus.FORBIDDEN.value(), "Access denied: Tenant schema does not exist.");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid tenant ID format: " + companySchema);
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            CompanyContextHolder.clear();
        }
    }
}
