package com.upgrade.campsite.application;


import com.upgrade.campsite.domain.model.AvailabilityEntity;
import com.upgrade.campsite.domain.model.ReservationEntity;
import com.upgrade.campsite.domain.model.exceptions.InvalidDatesException;
import com.upgrade.campsite.domain.model.exceptions.ReservationFailedException;
import com.upgrade.campsite.domain.model.exceptions.ReservationNotFoundException;
import com.upgrade.campsite.interfaces.dto.ReservationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final Logger log = LoggerFactory.getLogger(ReservationController.class);

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Display all availabilities for a range of dates
     * format "2000-10-31" where the capacity is > 0.
     */
    @GetMapping(path = "/availabilities")
    ResponseEntity<Object> availabilities(
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        try {
            if (!reservationService.availabilities(fromDate, toDate).isEmpty())
                return ResponseEntity.status(HttpStatus.FOUND).body(reservationService.availabilities(fromDate, toDate));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(reservationService.availabilities(fromDate, toDate));

        } catch (InvalidDatesException e) {
            log.debug(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    /**
     * Reserve the campsite for a given date
     */
    @PostMapping("/reservation")
    ResponseEntity<Object> reservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO reservation = reservationService.reserveCampfromTo(reservationDTO);
            return ResponseEntity.ok(reservation);
        } catch (InvalidDatesException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ReservationFailedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * update a reservation
     */
    @PutMapping("/reservation")
    ResponseEntity<Object> reservation(@PathVariable UUID id, @RequestBody ReservationDTO newReservation) {
        Map<String, String> response = new HashMap<String, String>();
        try {
            ReservationDTO updatedReservation = reservationService.updateReservation(id, newReservation);
            return ResponseEntity.status(HttpStatus.OK).body(updatedReservation);

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
    @DeleteMapping(path = "/cancel", produces = "application/json")
    ResponseEntity<Map<String, String>> cancel(@RequestParam UUID id) {
        Map<String, String> response = new HashMap<String, String>();
        try {
            reservationService.deleteReservation(id);
            response.put("ok", "success cancelling reservation");
            return ResponseEntity.accepted().body(response);

        } catch (ReservationNotFoundException e) {
            log.debug(e.getMessage());
            response.put("error", "Reservation Not Found for id:" + id);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/reservations")
    List<ReservationEntity> all() {
        return reservationService.all();
    }

    @GetMapping("/avas")
    List<AvailabilityEntity> avas() {
        return reservationService.allAvailabilities();
    }

}
