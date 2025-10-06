package com.touin.thierry.sb.test.springboottester.infrastructure;

import com.touin.thierry.sb.test.springboottester.domain.HelloWorlAdapteur;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("mock")
public class HelloWorldMockAdapter implements HelloWorlAdapteur {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldMockAdapter.class);

    @Override
    public String sayHello() {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("sayHello()");
        }                

        return "Hello from mock adapter";
    }
}
