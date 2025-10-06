package com.touin.thierry.sb.test.springboottester.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HelloWorldUseCaseImpl implements HelloWorldUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldUseCaseImpl.class);

    @Autowired
    private HelloWorlAdapteur helloWorlAdapteur;

    @Override
    public String sayHello() {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("sayHello()");
        }        

        return helloWorlAdapteur.sayHello();
    }

}
