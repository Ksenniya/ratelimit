package com.test.ratelimit.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitServiceImpl implements RateLimitService {

    @Value("${api.capacity}")
    private Integer capacity;
    @Value("${api.duration}")
    private Integer duration;

    Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    @Override
    public Bucket resolveBucket(String ipAddress) {
        return bucketCache.computeIfAbsent(ipAddress, this::newBucket);
    }

    private Bucket newBucket(String s) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(capacity, Refill.greedy(capacity/(capacity/10+1), Duration.ofMinutes(1)))).build();
    }
}