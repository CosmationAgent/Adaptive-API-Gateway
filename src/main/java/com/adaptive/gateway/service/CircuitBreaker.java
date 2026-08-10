package com.adaptive.gateway.service;

public class CircuitBreaker {
    public enum State { CLOSED, OPEN, HALF_OPEN }

    private State state = State.CLOSED;
    private int failureCount = 0;
    private final int failureThreshold;
    private final long openTimeoutMillis;
    private long lastOpenedTime = 0;

    public CircuitBreaker(int failureThreshold, long openTimeoutMillis) {
        this.failureThreshold = failureThreshold;
        this.openTimeoutMillis = openTimeoutMillis;
    }

    public synchronized boolean allowRequest() {
        if (state == State.OPEN) {
            if (System.currentTimeMillis() - lastOpenedTime > openTimeoutMillis) {
                state = State.HALF_OPEN;
                return true;
            }
            return false;
        }
        return true;
    }

    public synchronized void recordSuccess() {
        failureCount = 0;
        if (state == State.HALF_OPEN) {
            state = State.CLOSED;
        }
    }

    public synchronized void recordFailure() {
        failureCount++;
        if (failureCount >= failureThreshold) {
            state = State.OPEN;
            lastOpenedTime = System.currentTimeMillis();
        }
    }

    public synchronized State getState() {
        return state;
    }
}