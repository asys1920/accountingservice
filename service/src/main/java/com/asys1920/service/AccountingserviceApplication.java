package com.asys1920.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.validation.Validator;

@EntityScan("com.asys1920.*")
@SpringBootApplication
public class AccountingserviceApplication{

	private final Validator validator;

	public AccountingserviceApplication(@Qualifier("defaultValidator") Validator validator) {
		this.validator = validator;
	}

	public static void main(String[] args) {
		SpringApplication.run(AccountingserviceApplication.class, args);
	}

}

