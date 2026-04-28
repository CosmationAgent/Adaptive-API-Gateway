package com.adaptive.gateway.service;

public interface IRateLimiter {
    boolean allowRequest(String clientId, int tokens);

    int getCapacity();

    double getRefillRatePerSecond();
}
