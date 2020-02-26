package com.asys1920.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.asys1920.*")
@SpringBootApplication
public class AccountingServiceApplication {

	public AccountingServiceApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(AccountingServiceApplication.class, args);
	}

}

