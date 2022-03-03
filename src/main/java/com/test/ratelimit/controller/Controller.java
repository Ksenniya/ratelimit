package com.test.ratelimit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class Controller {
    /**
     * Example method for ip rate limit
     *
     * @return
     */
    @GetMapping(value = "/rate-limit")
    public ResponseEntity<?> checkRateLimit() {
        return ResponseEntity.ok().build();
    }
}