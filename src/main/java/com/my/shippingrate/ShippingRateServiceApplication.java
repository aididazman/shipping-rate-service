package com.my.shippingrate;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Springboot back-end shipping-rate-service",
				description = "Back-end shipping-rate-service REST API Documentation",
				version = "v1.0",
				contact = @Contact(
						name = "Muhammad Aidid",
						email = "aididazman98@gmail.com",
						url = "https://www.linkedin.com/in/muhammadaididazman/"
				)
		)
)
public class ShippingRateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShippingRateServiceApplication.class, args);
	}

}
