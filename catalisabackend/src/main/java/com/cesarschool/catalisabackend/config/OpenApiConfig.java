package com.cesarschool.catalisabackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Catalisa")
                        .version("1.0.0")
                        .description("Documentação da API do projeto Catalisa")
                        .contact(new Contact()
                                .name("Equipe Catalisa")
                                .email("gasab@cesar.school")));
    }
}