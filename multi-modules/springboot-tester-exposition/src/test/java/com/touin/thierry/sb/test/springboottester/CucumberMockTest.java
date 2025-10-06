package com.touin.thierry.sb.test.springboottester;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
  plugin = {"pretty"},
  features = "src/test/resources/features/mock",
  glue = {
  "com.touin.thierry.sb.test.springboottester",
  "com.touin.thierry.sb.test.springboottester.steps"
  }
)
public class CucumberMockTest {

}
