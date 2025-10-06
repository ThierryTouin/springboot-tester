package com.touin.thierry.sb.test.springboottester.infrastructure.externalservice;

import org.springframework.stereotype.Component;

@Component
public class ExternalServiceClient {

    public ExternalDto getById(String id) {
        // Simule un appel externe
        try {
            Thread.sleep(100); // latence fictive
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new ExternalDto(id, "value for " + id);
    }
}
