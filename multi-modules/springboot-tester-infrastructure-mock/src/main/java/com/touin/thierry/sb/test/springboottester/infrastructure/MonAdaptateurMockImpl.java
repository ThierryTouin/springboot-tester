package com.touin.thierry.sb.test.springboottester.infrastructure;

import com.touin.thierry.sb.test.springboottester.domain.MonAdaptateur;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.springframework.stereotype.Service;

//@Service
public class MonAdaptateurMockImpl implements MonAdaptateur {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonAdaptateurMockImpl.class);

    public MonAdaptateurMockImpl() {
        System.out.println("MonAdaptateurMockImpl started !!!");        
    } 

    public void doSomething() {   
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("doSomething()");
        }
    }

}
