package com.didacto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@SpringBootApplication
public class DidactoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DidactoApplication.class, args);
	}

}
