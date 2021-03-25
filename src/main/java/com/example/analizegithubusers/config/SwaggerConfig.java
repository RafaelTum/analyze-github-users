package com.example.analizegithubusers.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Analyze GitHub Users",
                version = "v1",
                description = "API for retrieving GitHub Users",
                contact = @Contact(name = "Rafael", email = "r.tumasyan@gmail.com")))
@Configuration
public class SwaggerConfig {
}
