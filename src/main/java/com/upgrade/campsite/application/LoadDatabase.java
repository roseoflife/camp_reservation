package com.upgrade.campsite.application;

import com.upgrade.campsite.dao.repositories.AvailabilityRepository;
import com.upgrade.campsite.dao.repositories.ReservationRepository;
import com.upgrade.campsite.domain.model.AvailabilityEntity;
import com.upgrade.campsite.domain.model.ReservationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Component
public class LoadDatabase implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    UUID uid1 = UUID.fromString("83b0fdc8-acf7-11eb-8529-0242ac130003");
    UUID uid2 = UUID.fromString("9016a39c-acf7-11eb-8529-0242ac130003");
    UUID uid3 = UUID.fromString("9016a39c-acf7-11eb-8529-0242ac130003");
    Random r = new Random();

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AvailabilityRepository availabilityRepo;

    @Override
    public void run(String... args) throws Exception {

        log.info(
                "Loading" + availabilityRepo.save(new AvailabilityEntity(r.nextLong(), LocalDate.now(), 1)));
        log.info(
                "Loading" + availabilityRepo.save(new AvailabilityEntity(r.nextLong(), LocalDate.now().plusDays(1), 1)));
        log.info(
                "Loading" + availabilityRepo.save(new AvailabilityEntity(r.nextLong(), LocalDate.now().plusDays(2), 1)));
        log.info(
                "Loading" + reservationRepository.save(new ReservationEntity(uid1, "Jack", "jack@upgrade.com", LocalDate.now(), LocalDate.now().plusDays(1)))
        );
        log.info(
                "Loading" + reservationRepository.save(new ReservationEntity(uid2, "Joe", "joe@upgrade.com", LocalDate.now(), LocalDate.now().plusDays(2)))
        );
    }

}

