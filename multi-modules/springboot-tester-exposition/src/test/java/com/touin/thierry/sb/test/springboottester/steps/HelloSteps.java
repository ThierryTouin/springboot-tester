package com.touin.thierry.sb.test.springboottester.steps;

import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.touin.thierry.sb.test.application.service.HelloWorldService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HelloSteps {

    @Autowired
    HelloWorldService helloWorldService;

    String result;

    @Given("le service Hello est prêt")
    public void le_service_hello_est_pret() {
        // Auto-injecté
    }

    @When("j'exécute le service Hello")
    public void j_execute_le_service_hello() {
        result = helloWorldService.execute();
    }

    @Then("le résultat est {string}")
    public void le_resultat_est(String expected) {
        assertEquals(expected, result);
    }
}
