package com.medvita.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.medvita.backend.repositories")
@EntityScan(basePackages = "com.medvita.backend.entities")
@ComponentScan(basePackages = {
        "com.medvita.backend.controllers",
        "com.medvita.backend.services",
        "com.medvita.backend.mappers"  // Make sure mappers package is scanned
})
public class MedvitaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedvitaBackendApplication.class, args);
    }

}
