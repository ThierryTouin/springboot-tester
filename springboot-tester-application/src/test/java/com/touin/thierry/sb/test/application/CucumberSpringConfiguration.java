package com.touin.thierry.sb.test.application;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest
public class CucumberSpringConfiguration {
    // Nécessaire pour que Cucumber démarre Spring Boot
}

