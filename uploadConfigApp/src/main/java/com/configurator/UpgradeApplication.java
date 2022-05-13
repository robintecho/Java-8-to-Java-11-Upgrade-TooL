package com.configurator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan("com.configurator")
@Component
public class UpgradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpgradeApplication.class, args);
	}

}
