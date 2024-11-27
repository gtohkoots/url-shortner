package com.org.urlshortner.repository;

import com.org.urlshortner.model.Url;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<Url, String> {

    // Custom query method to find a URL by its shortUrl field
    Url findByShortUrl(String shortUrl);

    Url findByOriginalUrl(String originalUrl);
}
