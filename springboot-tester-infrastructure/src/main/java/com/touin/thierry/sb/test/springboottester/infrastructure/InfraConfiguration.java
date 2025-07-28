package com.touin.thierry.sb.test.springboottester.infrastructure;

//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.touin.thierry.sb.test.springboottester.domain.MonAdaptateur;
import com.touin.thierry.sb.test.springboottester.infrastructure.externalservice.ExternalServicePort;

@Configuration
@Profile("!mock")
public class InfraConfiguration {

    @Bean
    //@ConditionalOnMissingBean(MonAdaptateur.class)
    public MonAdaptateur myService(MyAppPropertiesV2 props, ExternalServicePort externalServicePort) {
        return new MonAdaptateurImpl(props, externalServicePort);
    }

    @Bean
    public HelloWorldRealAdapter myHelloWorldRealAdapter(MyAppPropertiesV2 props, ExternalServicePort externalServicePort) {
        return new HelloWorldRealAdapter();
    }

}
