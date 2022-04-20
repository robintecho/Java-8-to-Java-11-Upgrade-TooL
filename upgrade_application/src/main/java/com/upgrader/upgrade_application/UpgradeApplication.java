package com.upgrader.upgrade_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan("com.upgrader")
@Component
public class UpgradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpgradeApplication.class, args);
	}

}
