package com.asys1920.accountingservice;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EntityScan("com.asys1920.*")
@EnableSwagger2
@SpringBootApplication
public class AccountingserviceApplication {

	private final Validator validator;

	public AccountingserviceApplication(@Qualifier("defaultValidator") Validator validator) {
		this.validator = validator;
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
	public static void main(String[] args) {
		SpringApplication.run(AccountingserviceApplication.class, args);
	}

}
