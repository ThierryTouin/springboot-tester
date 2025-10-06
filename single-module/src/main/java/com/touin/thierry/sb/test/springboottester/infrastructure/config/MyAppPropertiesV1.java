package com.touin.thierry.sb.test.springboottester.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "infrastructure")
public class MyAppPropertiesV1 {

    private Feature feature = new Feature();
    private Business business = new Business();

    public static class Feature {
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        

    }

    public static class Business {
        private int threshold;

        public int getThreshold() {
            return threshold;
        }

        public void setThreshold(int threshold) {
            this.threshold = threshold;
        }
 
        

    }

    public Feature getFeature() {
        return feature;
    }

    public Business getBusiness() {
        return business;
    }


}
