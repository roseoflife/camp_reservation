package com.upgrade.campsite.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({"com.upgrade.campsite.application"})
@EntityScan("com.upgrade.campsite.domain.model")
@EnableJpaRepositories("com.upgrade.campsite.infrastructure.repositories")
@SpringBootApplication
public class CampReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampReservationApplication.class, args);
    }

}
