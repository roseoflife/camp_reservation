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

    /**
     * Display all availabilities for a range of dates
     * format "2000-10-31" where the capacity is > 0.
     */
    //todo: add the default for 1 month
    @GetMapping("/findAvailabilities")
    List<AvailabilityEntity> findAvailabilities(
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        return availabilityRepository.findAvailfromTo(fromDate, toDate);
    }

    /**
     * Reserve the campsite for a given date
     */
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

    /**
     * update a reservation
     */
    @PutMapping("/reservation/{id}")
    ResponseEntity<Object> updateReservation(@PathVariable UUID id, @RequestBody ReservationDTO newReservation) {
        try {
            ReservationDTO updatedReservation = reservationService.updateReservation(id, newReservation);
            //TODO: update the HttpStatus
            return ResponseEntity.status(HttpStatus.FOUND).body(updatedReservation);

        } catch (InvalidDatesException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ReservationNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Cancel a reservation
     */
    @DeleteMapping("/reservation/{id}")
    ResponseEntity<Object> deleteReservation(@PathVariable UUID id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(id);

        } catch (ReservationNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/reservations")
    List<ReservationEntity> all() {
        return reservationRepository.findAll();
    }

}
