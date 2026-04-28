package com.adaptive.gateway.controller;

import com.adaptive.gateway.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.adaptive.gateway.models.QueuePriorityType;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final RateLimiter rateLimiter;
    private final AsyncRequestQueueService queueService;

    public GatewayController(RateLimiter rateLimiter, AsyncRequestQueueService queueService) {
        this.rateLimiter = rateLimiter;
        this.queueService = queueService;
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

    @PostMapping("/submit")
    public ResponseEntity<String> submitData(@RequestParam String clientId, @RequestParam(defaultValue = "NORMAL") QueuePriorityType priority, @RequestBody String data) {
        boolean allowed = rateLimiter.allowRequest(clientId, 1);
        if (allowed) {
            // Simulate queuing the request for async processing
            String requestId = queueService.submit(clientId, data, priority);
            return ResponseEntity.accepted().body("Request queued with ID: " + requestId);
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded for client: " + clientId);
        }
    }

    @GetMapping("/result/{requestId}")
    public ResponseEntity<String> getResult(@PathVariable String requestId) {
        AsyncRequestQueueService.Status status = queueService.getStatus(requestId);
        if (status != null) {
            return ResponseEntity.ok("Request ID: " + requestId + " Status: " + status);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request ID not found: " + requestId);
        }
    }
}