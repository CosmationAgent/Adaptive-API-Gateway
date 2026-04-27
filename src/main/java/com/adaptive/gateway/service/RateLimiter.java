package com.adaptive.gateway.service;

import org.springframework.stereotype.Service;

@Service
public class RateLimiter implements IRateLimiter {
    
    @Override
    public boolean allowRequest(String clientId) {
        // Implement rate limiting logic here
        return true; // Placeholder: allow all requests for now
    }
}
