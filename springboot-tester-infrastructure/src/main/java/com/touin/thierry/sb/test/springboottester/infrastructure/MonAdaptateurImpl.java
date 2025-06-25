package com.touin.thierry.sb.test.springboottester.infrastructure;

import com.touin.thierry.sb.test.springboottester.domain.MonAdaptateur;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.springframework.stereotype.Service;

//@Service
public class MonAdaptateurImpl implements MonAdaptateur {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonAdaptateurImpl.class);

    private final MyAppPropertiesV2 props;

    public MonAdaptateurImpl(MyAppPropertiesV2 props) {
        this.props = props;
        System.out.println("MonAdaptateurImpl started !!!");        
    } 

    public void doSomething() {
        if (props.getFeatureEnabled()) {
            
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("features actived  !");
            }
        } else {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("features NOT actived !");
            }
        }

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("PostalCodeServiceEndpoint:" + props.getPostalCodeServiceEndpoint());
            }

    }

}
