package com.touin.thierry.sb.test.springboottester.infrastructure;

import com.touin.thierry.sb.test.springboottester.domain.HelloWorldUseCase;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("mock")
public class HelloWorldMockAdapter implements HelloWorldUseCase {

    @Override
    public String sayHello() {
        return "Hello from mock adapter";
    }
}
