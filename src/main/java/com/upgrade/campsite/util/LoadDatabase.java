package com.upgrade.campsite.util;

import com.upgrade.campsite.AvailabilityRepository;
import com.upgrade.campsite.ReservationRepository;
import com.upgrade.campsite.model.AvailabilityEntity;
import com.upgrade.campsite.model.ReservationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    UUID uid1 = UUID.fromString("83b0fdc8-acf7-11eb-8529-0242ac130003");
    UUID uid2 = UUID.fromString("9016a39c-acf7-11eb-8529-0242ac130003");
    UUID uid3 = UUID.fromString("9016a39c-acf7-11eb-8529-0242ac130003");
    Random r = new Random();


    @Bean
    CommandLineRunner
    initDatabase(ReservationRepository reservationRepository, AvailabilityRepository availabilityRepo) {
        return args -> {
            log.info(
                    "Loading" + availabilityRepo.save(new AvailabilityEntity(r.nextLong(), LocalDate.now(), 10)));
            log.info(
                    "Loading" + availabilityRepo.save(new AvailabilityEntity(r.nextLong(), LocalDate.now().plusDays(1), 10)));
            log.info(
                    "Loading" + availabilityRepo.save(new AvailabilityEntity(r.nextLong(), LocalDate.now().plusDays(2), 10)));
            log.info(
                    "Loading" + reservationRepository.save(new ReservationEntity(uid1, "Jack", "jack@upgrade.com", LocalDate.now(), LocalDate.now().plusDays(1)))
            );
            log.info(
                    "Loading" + reservationRepository.save(new ReservationEntity(uid2, "Joe", "joe@upgrade.com", LocalDate.now(), LocalDate.now().plusDays(2)))
            );
        };

    }


}
