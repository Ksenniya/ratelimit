package com.test.ratelimit.service;

import io.github.bucket4j.Bucket;

public interface RateLimitService {
    Bucket resolveBucket(String apiKey);
}
