package com.upgrader.upgrade_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.upgrader")
public class UpgradeApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpgradeApplication.class, args);
	}

}
