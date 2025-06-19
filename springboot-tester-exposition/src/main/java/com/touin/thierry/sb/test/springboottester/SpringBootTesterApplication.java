package com.touin.thierry.sb.test.springboottester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "com.touin.thierry.sb.test")
//@EnableConfigurationProperties(MyAppProperties.class)
public class SpringBootTesterApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootTesterApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(SpringBootTesterApplication.class, args);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Started ok 2 !!!");
		}
	}

}

