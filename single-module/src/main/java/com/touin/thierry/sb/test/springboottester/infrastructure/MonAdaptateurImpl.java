package com.touin.thierry.sb.test.springboottester.infrastructure;

import com.touin.thierry.sb.test.springboottester.domain.MonAdaptateur;
import com.touin.thierry.sb.test.springboottester.infrastructure.config.MyAppPropertiesV2;
import com.touin.thierry.sb.test.springboottester.infrastructure.externalservice.ExternalDto;
import com.touin.thierry.sb.test.springboottester.infrastructure.externalservice.ExternalServicePort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.springframework.stereotype.Service;

//@Service
public class MonAdaptateurImpl implements MonAdaptateur {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonAdaptateurImpl.class);

    private final MyAppPropertiesV2 props;

    private final ExternalServicePort externalServicePort;

    public MonAdaptateurImpl(MyAppPropertiesV2 props,ExternalServicePort externalServicePort) {
        this.props = props;
        this.externalServicePort = externalServicePort;
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

        ExternalDto externalDtoTiti = externalServicePort.getData("titi");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("externalDtoTiti:" + externalDtoTiti.value());
        }
        ExternalDto externalDtoToto = externalServicePort.getData("toto");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("externalDtoToto:" + externalDtoToto.value());
        }
    }

}
