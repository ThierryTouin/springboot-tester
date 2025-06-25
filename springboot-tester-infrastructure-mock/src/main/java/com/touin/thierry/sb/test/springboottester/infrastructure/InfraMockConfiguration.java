package com.touin.thierry.sb.test.springboottester.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;


import com.touin.thierry.sb.test.springboottester.domain.MonAdaptateur;

@Configuration
@Profile("mock")
public class InfraMockConfiguration {

    @Bean
    @Primary
    public MonAdaptateur myMonAdaptateurMockImpl() {
        return new MonAdaptateurMockImpl();
    }
}
