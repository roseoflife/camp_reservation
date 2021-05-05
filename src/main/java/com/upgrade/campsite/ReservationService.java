package com.upgrade.campsite;

import com.upgrade.campsite.Exceptions.InvalidDatesException;
import com.upgrade.campsite.dto.ReservationDTO;
import com.upgrade.campsite.model.AvailabilityEntity;
import com.upgrade.campsite.model.ReservationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);
    ReservationRepository reservationRepository;
    AvailabilityRepository availabilityRepository;

    public ReservationService(ReservationRepository reservationRepository, AvailabilityRepository availabilityRepository) {
        this.reservationRepository = reservationRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public ReservationDTO ReserveCampfromTo(ReservationDTO r) throws InvalidDatesException {
        List<AvailabilityEntity> availabilities;
        if (validateDates(r.getFromDate(), r.getToDate())) {
            availabilities = availabilityRepository.findAvailfromTo(
                    r.getFromDate(), r.getToDate());
            for (AvailabilityEntity availability : availabilities) {
                availability.setCapacity(availability.getCapacity() - 1);
            }
            //todo: Use an EntityMapper Here
            availabilityRepository.saveAll(availabilities);
            ReservationEntity reservation = new ReservationEntity(r.getUid(), r.getName(), r.getEmail(), r.getFromDate(), r.getToDate());
            reservationRepository.save(reservation);
            return r;
        } else
            throw new InvalidDatesException(r.getFromDate(), r.getToDate(), "Invalid Dates");
    }

    //toDo: Add validation if fromDate and ToDate are null
    public Boolean validateDates(LocalDate fromDate, LocalDate toDate) throws IllegalArgumentException {
        if (fromDate.isEqual(toDate)) {
            log.error("Reservation should be at least for one day");
            throw new InvalidDatesException(fromDate, toDate, "Reservation should be at least for one day");
        } else if (fromDate.isBefore(toDate)) {
            log.error("Departure date should be after arrival Date");
            throw new InvalidDatesException(fromDate, toDate, "Departure date should be after arrival Date");
        } else if (toDate.isAfter(fromDate.plusDays(3))) {
            log.error("Reservation is for maximum 3 days");
            throw new InvalidDatesException(fromDate, toDate, "Reservation is for maximum 3 days");
        } else
            return true;
    }
}

//findAvailfromTo
//ReserveCampfromTo
//modifyBooking
//cancelBooking

