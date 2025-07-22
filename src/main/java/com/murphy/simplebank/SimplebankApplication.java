package com.murphy.simplebank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Simplebank application",
				description = "Backend Rest APIs for Simplebank",
				version = "v1.0",
				contact =@Contact(
						name ="Daniel Murphy",
						email = "alaxzandaaungmyintmyat@gmail.com",
						url = "https://github.com/murphy0003/simplebank_app"
				),
				license = @License(
						name = "murphy03",
						url = "https://github.com/murphy0003/simplebank_app"
				)


		),
		externalDocs = @ExternalDocumentation(
				description = "Simple Bank App Documentation",
				url = "https://github.com/murphy0003/simplebank_app"
		)
)
public class SimplebankApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplebankApplication.class, args);
	}

}
