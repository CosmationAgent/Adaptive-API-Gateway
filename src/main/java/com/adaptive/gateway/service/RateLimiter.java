package com.adaptive.gateway.service;

public class RateLimiter implements IRateLimiter {

    private final int capacity;
    private final double refillRatePerSecond;
    private final RedisTokenBucketStore tokenBucketStore;

    public RateLimiter(int capacity, double refillRatePerSecond, RedisTokenBucketStore tokenBucketStore) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.tokenBucketStore = tokenBucketStore;
    }
    
    @Override
    public boolean allowRequest(String clientId, int tokens) {
        if (tokens <= 0) {
            return true;
        }
        return tokenBucketStore.allowRequestAtomic(
            clientId,
            tokens,
            capacity,
            refillRatePerSecond,
            System.currentTimeMillis()
        );
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public double getRefillRatePerSecond() {
        return refillRatePerSecond;
    }
}
