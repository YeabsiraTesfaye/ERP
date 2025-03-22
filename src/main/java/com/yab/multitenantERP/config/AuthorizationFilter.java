package com.yab.multitenantERP.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yab.multitenantERP.JwtUtil;
import com.yab.multitenantERP.entity.ApiEntity;
import com.yab.multitenantERP.entity.UserEntity;
import com.yab.multitenantERP.repositories.ApiEndpointRepository;
import com.yab.multitenantERP.repositories.UserRepository;
import com.yab.multitenantERP.services.AuthorizationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ApiEndpointRepository apiRepository;

    // Define paths that should be excluded from authorization checks
    private static final List<String> PERMITTED_PATHS = List.of(
            "/tenant/getTenants", "/auth/"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (PERMITTED_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println(path);
        String normalizedPath = path.replaceAll("/\\d+", "/{id}");
        System.out.println(normalizedPath);

        Optional<ApiEntity> apiOpt = apiRepository.findByPathAndMethod(normalizedPath, method);
        if (apiOpt.isEmpty()) {
            sendJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, "Endpoint not registered");
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        String username;
        try {
            Claims claims = jwtUtil.validate_token(token);
            username = claims.getSubject();
        } catch (JwtException e) {
            sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found");
            return;
        }

        UserEntity user = userOpt.get();

        if (!authorizationService.hasAccess(user, normalizedPath, method)) {
            sendJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendJsonResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("error", message)));
    }
}
