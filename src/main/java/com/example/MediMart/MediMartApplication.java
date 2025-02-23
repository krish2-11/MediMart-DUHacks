package com.example.MediMart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MediMartApplication {
	public static void main(String[] args) {
		SpringApplication.run(MediMartApplication.class, args);
	}
}
