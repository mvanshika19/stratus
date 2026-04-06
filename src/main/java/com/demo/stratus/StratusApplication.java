package com.demo.stratus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StratusApplication {

	public static void main(String[] args) {
		SpringApplication.run(StratusApplication.class, args);
	}

}
