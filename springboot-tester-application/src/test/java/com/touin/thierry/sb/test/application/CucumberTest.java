package com.touin.thierry.sb.test.application;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
  plugin = {"pretty"},
  features = "src/test/resources/features",
  glue = "com.touin.thierry.sb.test.application.steps"
)
public class CucumberTest {
}

