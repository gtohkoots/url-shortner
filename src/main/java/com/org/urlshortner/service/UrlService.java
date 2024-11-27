package com.org.urlshortner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.urlshortner.model.Url;
import com.org.urlshortner.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UrlService {

    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_PREFIX = "url:";

    public Url createShortUrl(String originalUrl) {
        // check if the original url is stored already
        Url lookUpUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (lookUpUrl != null) {
            logger.info("short url created already, returned from DB");
            return lookUpUrl;
        }
        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(UUID.randomUUID().toString().substring(0, 8)); // Generate short URL
        url.setCreatedAt(LocalDateTime.now());

        Url savedUrl = urlRepository.save(url);

        String cacheKey = CACHE_PREFIX + savedUrl.getShortUrl();
        redisTemplate.opsForValue().set(cacheKey, savedUrl, 1, TimeUnit.HOURS);
        return savedUrl;
    }

    // Retrieve original URL by short URL
    public Url getOriginalUrl(String shortUrl) {
        // Check if URL exists in Redis
        Object cachedUrl = redisTemplate.opsForValue().get(CACHE_PREFIX + shortUrl);
        if (cachedUrl != null) {
            logger.info("short url found in Redis");
            logger.info(cachedUrl.toString());
            return objectMapper.convertValue(cachedUrl, Url.class);
        }

        // If not in Redis, retrieve from database
        Url url = urlRepository.findByShortUrl(shortUrl);
        if (url != null) {
            logger.info("url not found in redis but found in mango DB, setting it up in Redis");
            // Cache the result in Redis with a TTL (e.g., 1 hour)
            redisTemplate.opsForValue().set(CACHE_PREFIX + shortUrl, url, 1, TimeUnit.HOURS);
        }
        return url;
    }
}
