package com.cesarschool.catalisabackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Catalisa API")
                        .version("v2")
                        .description("""
                                API responsável pelo gerenciamento de usuários, produtos, pesquisas,
                                consumos e indicadores analíticos do projeto Catalisa.
                                
                                Principais módulos:
                                - Users
                                - Products
                                - Pesquisas
                                - Consumos
                                - Dashboard
                                """)
                        .contact(new Contact()
                                .name("Equipe Catalisa")
                                .email("gasab@cesar.school"))
                        .license(new License()
                                .name("Uso acadêmico / interno")))
                .components(new Components());
    }
}