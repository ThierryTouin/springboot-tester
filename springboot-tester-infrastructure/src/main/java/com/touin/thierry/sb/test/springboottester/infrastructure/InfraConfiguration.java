package com.touin.thierry.sb.test.springboottester.infrastructure;

//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;

import com.touin.thierry.sb.test.springboottester.domain.MonAdaptateur;

@Configuration
//@Profile("!mock")
public class InfraConfiguration {

    @Bean
    //@ConditionalOnMissingBean(MonAdaptateur.class)
    public MonAdaptateur myService(MyAppPropertiesV2 props) {
        return new MonAdaptateurImpl(props);
    }
}
