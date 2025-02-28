package com.example.facieaiprojecttest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Product API",
                version = "1.0",
                description = "API for managing products.",
                contact = @Contact(name = "Tymofii Shovkun", email = "shovkyntimofej@gmail.com")
        )
)
public class SwaggerConfig {
}

