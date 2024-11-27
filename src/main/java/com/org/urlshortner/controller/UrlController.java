package com.org.urlshortner.controller;

import com.org.urlshortner.model.Url;
import com.org.urlshortner.service.RedisHealthService;
import com.org.urlshortner.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);

    @Autowired
    UrlService urlService;

    @Autowired
    RedisHealthService redisHealthService;

    // Create a short URL
    @PostMapping("/shorten")
    public Url shortenUrl(@RequestBody String originalUrl) {
        logger.info("SHORTENING ORIGINAL URL");
        return urlService.createShortUrl(originalUrl);
    }

    @GetMapping("/")
    public String healthCheck() {
        return "Endpoint is valid";
    }

    // Retrieve the original URL
    @GetMapping("/{shortUrl}")
    @ResponseBody
    public Url getOriginalUrl(@PathVariable String shortUrl) {
        logger.info("FETCHING ORIGINAL URL");
        return urlService.getOriginalUrl(shortUrl);
    }

}
