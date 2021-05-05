package com.upgrade.campsite;


import com.upgrade.campsite.Exceptions.InvalidDatesException;
import com.upgrade.campsite.Exceptions.ReservationNotFoundException;
import com.upgrade.campsite.dto.ReservationDTO;
import com.upgrade.campsite.model.AvailabilityEntity;
import com.upgrade.campsite.model.ReservationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ReservationService reservationService;
    private final Logger log = LoggerFactory.getLogger(ReservationController.class);


    public ReservationController(ReservationRepository reservationRepository, AvailabilityRepository availabilityRepository, ReservationService reservationService) {
        this.reservationRepository = reservationRepository;
        this.availabilityRepository = availabilityRepository;
        this.reservationService = reservationService;
    }

    /*
    Display all availabilities
     */
    @GetMapping("/availabilities")
    List<AvailabilityEntity> availabilities() {
        return availabilityRepository.findAll();
    }


    /**
     * Display all availabilities for a range of dates
     * format "2000-10-31" where the capacity is >0.
     */
    @GetMapping("/findAvailabilities")
    List<AvailabilityEntity> findAvailabilities(
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return availabilityRepository.findAvailfromTo(fromDate, toDate);
    }

    @GetMapping("/reservations")
    List<ReservationEntity> all() {
        return reservationRepository.findAll();
    }

    @PostMapping("/reserve")
    ResponseEntity<Object> reservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO reservation = reservationService.ReserveCampfromTo(reservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (InvalidDatesException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/reservation/{id}")
    ReservationEntity findOne(@PathVariable UUID id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException(id));

    }

    /*
    update a reservation
     */
    @PutMapping("/reservation/{id}")
    ReservationEntity updateReservation(@RequestBody ReservationEntity newReservation, @PathVariable UUID id) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setEmail(newReservation.getEmail());
                    reservation.setFromDate(newReservation.getFromDate());
                    reservation.setToDate(newReservation.getToDate());
                    reservation.setName(newReservation.getName());
                    return reservationRepository.save(reservation);
                })
                .orElseGet(() -> {
                    newReservation.setUid(id);
                    return reservationRepository.save(newReservation);
                });
    }

    @DeleteMapping("/reservation/{id}")
    void deleteReservation(@PathVariable UUID id) {
        reservationRepository.deleteById(id);
    }


}
