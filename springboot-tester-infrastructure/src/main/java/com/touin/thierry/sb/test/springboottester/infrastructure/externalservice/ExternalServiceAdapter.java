package com.touin.thierry.sb.test.springboottester.infrastructure.externalservice;


import com.github.benmanes.caffeine.cache.Cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ExternalServiceAdapter implements ExternalServicePort {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalServiceAdapter.class);

    private final ExternalServiceClient client;
    private final Cache<String, ExternalDto> cache;

    public ExternalServiceAdapter(ExternalServiceClient client,
                                   Cache<String, ExternalDto> cache) {
        this.client = client;
        this.cache = cache;
    }

    @Override
    public ExternalDto getData(String id) {
        ExternalDto cached = cache.getIfPresent(id);
        if (cached != null) {
            LOGGER.info("Returned from cache for id {}", id);
            return cached;
        }

        LOGGER.info("Calling external service for id {}", id);
        ExternalDto data = client.getById(id);
        cache.put(id, data);
        return data;
    }
}
