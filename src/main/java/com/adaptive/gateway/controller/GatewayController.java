package com.adaptive.gateway.controller;

import com.adaptive.gateway.service.RateLimiter;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final RateLimiter rateLimiter;

    public GatewayController(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getGatewayStatus() {
        return ResponseEntity.ok("Gateway is running");
    }

    @GetMapping("/rate-limit-test")
    public ResponseEntity<String> testRateLimiting(@RequestParam String clientId) {
        boolean allowed = rateLimiter.allowRequest(clientId, 1);
        if (allowed) {
            return ResponseEntity.ok("Request allowed for client: " + clientId);
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded for client: " + clientId);
        }
    }
}