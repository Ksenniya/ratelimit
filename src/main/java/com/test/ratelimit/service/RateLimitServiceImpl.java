package com.test.ratelimit.service;

import com.test.ratelimit.bucket.SimpleBucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitServiceImpl implements RateLimitService {

    @Value("${api.capacity}")
    private Integer capacity;
    @Value("${api.duration}")
    private Integer duration;

    Map<String, SimpleBucket> bucketCache = new ConcurrentHashMap<>();

    @Override
    public SimpleBucket resolveBucket(String ipAddress) {
        return bucketCache.computeIfAbsent(ipAddress, this::newBucket);
    }

    /*

    With bucket4j Bucket would be:

    private Bucket newBucket(String s) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(capacity, Duration.ofMinutes(duration)))).build();
    }
    */

    private SimpleBucket newBucket(String s) {
        return new SimpleBucket(capacity, duration);
    }
}