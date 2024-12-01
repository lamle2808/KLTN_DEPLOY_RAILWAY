package com.example.kltn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "KLTN API", version = "1.0", description = "api for fe Event Ease"))
public class KltnApplication {

	public static void main(String[] args) {
		SpringApplication.run(KltnApplication.class, args);
	}

}
