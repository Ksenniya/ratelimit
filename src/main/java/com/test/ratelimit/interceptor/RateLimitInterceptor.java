package com.test.ratelimit.interceptor;

import com.test.ratelimit.bucket.ConsumptionProbe;
import com.test.ratelimit.bucket.SimpleBucket;
import com.test.ratelimit.service.RateLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    @Value("${api.capacity}")
    private Integer capacity;
    @Autowired
    private RateLimitService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (capacity < 1) {
            response.sendError(HttpStatus.BAD_GATEWAY.value());
            return false;
        }
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        SimpleBucket tokenBucket = rateLimiterService.resolveBucket(ipAddress);
        ConsumptionProbe probe = tokenBucket.tryConsume();
        if (probe.isConsumed()) {
            return true;
        } else {
            response.sendError(HttpStatus.BAD_GATEWAY.value());
            return false;
        }
    }
}



