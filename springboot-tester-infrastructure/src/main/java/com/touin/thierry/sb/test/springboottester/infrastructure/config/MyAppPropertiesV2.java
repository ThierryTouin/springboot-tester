package com.touin.thierry.sb.test.springboottester.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyAppPropertiesV2 {

    @Value("${infrastructure.feature.enabled}")
    private boolean featureEnabled;

    public boolean getFeatureEnabled() {
        return featureEnabled;
    }

    @Value("${infrastructure.services.rest.postal-code.endpoint}")
    private String postalCodeServiceEndpoint;

    public String getPostalCodeServiceEndpoint() {
        return postalCodeServiceEndpoint;
    }

}
