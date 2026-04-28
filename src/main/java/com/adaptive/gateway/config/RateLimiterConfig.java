package com.adaptive.gateway.config;

import com.adaptive.gateway.service.RedisTokenBucketStore;
import com.adaptive.gateway.service.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class RateLimiterConfig {
    @Bean
    public RateLimiter rateLimiter(
        @Value("${ratelimiter.capacity:10}") int capacity,
        @Value("${ratelimiter.refillRatePerSecond:1.0}") double refillRatePerSecond,
        RedisTokenBucketStore tokenBucketStore
    ) {
        return new RateLimiter(capacity, refillRatePerSecond, tokenBucketStore);
    }
}