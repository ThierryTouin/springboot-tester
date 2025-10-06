package com.touin.thierry.sb.test.springboottester.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.touin.thierry.sb.test.springboottester.domain.HelloWorlAdapteur;


public class HelloWorldRealAdapter implements HelloWorlAdapteur {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldRealAdapter.class);

    @Override
    public String sayHello() {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("sayHello()");
        }        

        return "Hello from real adapter";
    }
}
