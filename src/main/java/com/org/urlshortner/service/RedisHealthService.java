package com.org.urlshortner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisHealthService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public String pingRedis() {
        try {
            String response = redisTemplate.execute((RedisCallback<String>) RedisConnectionCommands::ping);
            if ("PONG".equals(response)) {
                return "Redis is up and running!";
            } else {
                return "Unexpected response from Redis: " + response;
            }
        } catch (Exception e) {
            return "Redis connection failed: " + e.getMessage();
        }
    }
}

