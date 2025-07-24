package com.touin.thierry.sb.test.application.service;

import com.touin.thierry.sb.test.domain.HelloWorldUseCase;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

    private final HelloWorldUseCase helloWorldUseCase;

    public HelloWorldService(HelloWorldUseCase helloWorldUseCase) {
        this.helloWorldUseCase = helloWorldUseCase;
    }

    public String execute() {
        return helloWorldUseCase.sayHello();
    }
}