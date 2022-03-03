package com.test.ratelimit.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
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
    public Bucket resolveBucket(String apiKey) {
        return bucketCache.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String s) {
        return Bucket.builder()
                .addLimit(Bandwidth.simple(capacity, Duration.ofMinutes(duration))).build();
    }
}