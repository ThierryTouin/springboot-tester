package com.touin.thierry.sb.test.springboottester.infrastructure;

import com.touin.thierry.sb.test.springboottester.domain.HelloWorldUseCase;


public class HelloWorldRealAdapter implements HelloWorldUseCase {

    @Override
    public String sayHello() {
        return "Hello from real adapter";
    }
}
