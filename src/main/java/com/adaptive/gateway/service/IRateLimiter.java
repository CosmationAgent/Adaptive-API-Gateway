package com.adaptive.gateway.service;

public interface IRateLimiter {
    boolean allowRequest(String clientId);
}
