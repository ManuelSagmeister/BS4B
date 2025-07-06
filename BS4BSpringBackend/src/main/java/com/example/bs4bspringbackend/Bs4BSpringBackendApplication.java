package com.example.bs4bspringbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Bs4BSpringBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Bs4BSpringBackendApplication.class, args);
	}

}
