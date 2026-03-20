package com.cesarschool.catalisabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
        basePackages = {
                "com.cesarschool.catalisabackend.controllers",
                "com.cesarschool.catalisabackend.services",
                "com.cesarschool.catalisabackend.utils",
                "com.cesarschool.catalisabackend.dtos",
                "com.cesarschool.catalisabackend.repositories",
                "com.cesarschool.catalisabackend.config"
                // + "com.cesarschool.catalisabackend.security" etc.
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com\\.cesarschool\\.catalisabackend\\.models\\.v1Antiga\\..*"
        )
)
@EntityScan(basePackages = {
        "com.cesarschool.catalisabackend.models"
})
@EnableJpaRepositories(basePackages = {
        "com.cesarschool.catalisabackend.repositories"
})
public class CatalisabackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalisabackendApplication.class, args);
    }
}
