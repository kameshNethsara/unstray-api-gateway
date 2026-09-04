package com.unstray.platform.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.unstray.platform.api_gateway.util.JwtUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            // =========================================================================
            // JWT VALIDATION DISABLED MODE (Commented out to bypass CORS / Auth blocks)
            // =========================================================================
            /*
            if (validator.isSecured.test(exchange.getRequest())) {

                // Safely grab the first authorization header
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                // If it's null or missing, block the request
                if (authHeader == null) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                // Clean the "Bearer " prefix
                if (authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }

                // Validate the token
                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            */

            // Pass through all requests directly without JWT token verification
            return chain.filter(exchange);
        });
    }
}