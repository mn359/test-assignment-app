package com.example.testassignmentapp;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig  {

    @Bean
    public OpenAPI appOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("TestAssignmentApp API Docs")
                        .description("TestAssignmentApp API documentation")
                        .version("v1.0.0"));
    }
}
