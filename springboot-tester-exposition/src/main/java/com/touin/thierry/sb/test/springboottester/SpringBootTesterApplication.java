package com.touin.thierry.sb.test.springboottester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.touin.thierry.sb.test")
public class SpringBootTesterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTesterApplication.class, args);
	}

}

