package com.org.urlshortner.controller;

import com.org.urlshortner.service.RedisHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Autowired
    private RedisHealthService redisHealthService;

    @GetMapping("/redis-health")
    public String checkRedisHealth() {
        return redisHealthService.pingRedis();
    }
}

