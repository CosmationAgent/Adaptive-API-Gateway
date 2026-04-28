package com.adaptive.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RedisTokenBucketStore {

    private static final DefaultRedisScript<Long> ALLOW_REQUEST_SCRIPT = createAllowRequestScript();

    private final StringRedisTemplate redisTemplate;
    private static final String BUCKET_KEY_PREFIX = "ratelimit:bucket:";
    private static final String TIMESTAMP_KEY_PREFIX = "ratelimit:timestamp:";
    private static final long TTL_SECONDS = 24 * 60 * 60;

    public RedisTokenBucketStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean allowRequestAtomic(String clientId,
                                      int requestedTokens,
                                      int capacity,
                                      double refillRatePerSecond,
                                      long nowMillis) {
        String bucketKey = BUCKET_KEY_PREFIX + clientId;
        String timestampKey = TIMESTAMP_KEY_PREFIX + clientId;

        Long result = redisTemplate.execute(
            ALLOW_REQUEST_SCRIPT,
            List.of(bucketKey, timestampKey),
            String.valueOf(requestedTokens),
            String.valueOf(capacity),
            String.valueOf(refillRatePerSecond),
            String.valueOf(nowMillis),
            String.valueOf(TTL_SECONDS)
        );

        boolean allowed = result != null && result == 1L;
        if (!allowed) {
            log.debug("Rate limit denied for clientId={}", clientId);
        }
        return allowed;
    }

    private static DefaultRedisScript<Long> createAllowRequestScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptText(
            "local bucketKey = KEYS[1] " +
            "local timestampKey = KEYS[2] " +
            "local requested = tonumber(ARGV[1]) " +
            "local capacity = tonumber(ARGV[2]) " +
            "local refillRate = tonumber(ARGV[3]) " +
            "local nowMillis = tonumber(ARGV[4]) " +
            "local ttlSeconds = tonumber(ARGV[5]) " +
            "local currentTokens = tonumber(redis.call('GET', bucketKey)) " +
            "local lastRefill = tonumber(redis.call('GET', timestampKey)) " +
            "if (not currentTokens) or (not lastRefill) then " +
            "  currentTokens = capacity " +
            "  lastRefill = nowMillis " +
            "end " +
            "local elapsedMs = nowMillis - lastRefill " +
            "if elapsedMs < 0 then elapsedMs = 0 end " +
            "local tokensToAdd = math.floor((elapsedMs * refillRate) / 1000) " +
            "currentTokens = math.min(capacity, currentTokens + tokensToAdd) " +
            "local allowed = 0 " +
            "if currentTokens >= requested then " +
            "  currentTokens = currentTokens - requested " +
            "  allowed = 1 " +
            "end " +
            "redis.call('SETEX', bucketKey, ttlSeconds, tostring(currentTokens)) " +
            "redis.call('SETEX', timestampKey, ttlSeconds, tostring(nowMillis)) " +
            "return allowed"
        );
        return script;
    }
}