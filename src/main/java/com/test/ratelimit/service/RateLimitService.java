package com.test.ratelimit.service;


import com.test.ratelimit.bucket.SimpleBucket;

public interface RateLimitService {

    SimpleBucket resolveBucket(String ipAddress);
}
