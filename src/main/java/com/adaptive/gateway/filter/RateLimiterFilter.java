package com.adaptive.gateway.filter;

import com.adaptive.gateway.service.IRateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(1) // Execute before RequestLoggingFilter
public class RateLimiterFilter extends OncePerRequestFilter {

    private final IRateLimiter rateLimiter;

    public RateLimiterFilter(IRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientId = getClientId(request);

        boolean allowed = rateLimiter.allowRequest(clientId, 1);
        if (!allowed) {
            log.warn("Rate limit exceeded for client: {}", clientId);
            response.setStatus(429); // HTTP 429 Too Many Requests
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                "{\"error\": \"Rate limit exceeded\", \"clientId\": \"%s\"}", clientId
            ));
            return;
        }

        log.debug("Request allowed for client: {}", clientId);
        filterChain.doFilter(request, response);
    }

    private static String getClientId(HttpServletRequest request) {
        String apiKey = request.getHeader("X-API-Key");
        if (apiKey != null && !apiKey.isEmpty()) {
            return apiKey;
        }

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip rate limiting for health and actuator endpoints
        String path = request.getRequestURI();
        return path.startsWith("/health") || 
               path.startsWith("/actuator") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/api-docs");
    }
}