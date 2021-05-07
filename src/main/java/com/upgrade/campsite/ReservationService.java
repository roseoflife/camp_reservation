package com.upgrade.campsite;

import com.upgrade.campsite.Exceptions.InvalidDatesException;
import com.upgrade.campsite.Exceptions.ReservationNotFoundException;
import com.upgrade.campsite.converter.Mapper;
import com.upgrade.campsite.dto.ReservationDTO;
import com.upgrade.campsite.model.AvailabilityEntity;
import com.upgrade.campsite.model.ReservationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);
    Mapper mapper = new Mapper();
    private ReservationRepository reservationRepository;
    private AvailabilityRepository availabilityRepository;

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
                availabilityRepository.save(availability);
                availabilityRepository.flush();
            }
            //todo: Use an EntityMapper Here
            ReservationEntity reservation = new ReservationEntity(r.getUid(), r.getName(), r.getEmail(), r.getFromDate(), r.getToDate());
            reservationRepository.save(reservation);
            reservationRepository.flush();
            return r;
        } else
            throw new InvalidDatesException(r.getFromDate(), r.getToDate(), "Invalid Dates");
    }

    //toDo: Add validation if fromDate and ToDate are null
    public Boolean validateDates(LocalDate fromDate, LocalDate toDate) throws IllegalArgumentException {
        if (fromDate.isEqual(toDate)) {
            log.error("Reservation should be at least for one day");
            throw new InvalidDatesException(fromDate, toDate, "Reservation should be at least for one day");
        } else if (fromDate.isAfter(toDate)) {
            log.error("Departure date should be after arrival Date");
            throw new InvalidDatesException(fromDate, toDate, "Departure date should be after arrival Date");
        } else if (toDate.isAfter(fromDate.plusDays(3))) {
            log.error("Reservation is for maximum 3 days");
            throw new InvalidDatesException(fromDate, toDate, "Reservation is for maximum 3 days");
        } else
            return true;
    }

//todo: complete this function

    /**
     * find the exiting reservation and update it.
     *
     * @param newReservation
     * @param id
     * @return
     * @throws InvalidDatesException
     */

    public ReservationDTO updateReservation(UUID id, ReservationDTO newReservation) throws InvalidDatesException, ReservationNotFoundException {
        if (!reservationRepository.findById(id).isPresent())
            throw new ReservationNotFoundException(id);
        else {
            //first validate the new reservationDates
            List<AvailabilityEntity> availabilities;
            if (validateDates(newReservation.getFromDate(), newReservation.getToDate())) {
                availabilities = availabilityRepository.findAvailfromTo(
                        newReservation.getFromDate(), newReservation.getToDate());
                // update the availability
                for (AvailabilityEntity availability : availabilities) {
                    if (isDateWithinRange(availability.getDate(), newReservation.getFromDate(), newReservation.getToDate())) {
                        availability.setCapacity(availability.getCapacity() - 1);
                        availabilityRepository.save(availability);
                        availabilityRepository.flush();

                    }
                }
                // update the existing reservation in db
                reservationRepository.findById(id).ifPresent(
                        existingReservation -> {
                            existingReservation.setEmail(newReservation.getEmail());
                            existingReservation.setFromDate(newReservation.getFromDate());
                            existingReservation.setToDate(newReservation.getToDate());
                            existingReservation.setName(newReservation.getName());
                            reservationRepository.save((existingReservation));

                        });
            } else
                throw new InvalidDatesException(newReservation.getFromDate(), newReservation.getToDate(), "Invalid Dates");
            return mapper.convertToDto(reservationRepository.findById(id).get());
        }
    }

    public boolean isDateWithinRange(LocalDate testDate, LocalDate fromDate, LocalDate toDate) {
        return !(testDate.isBefore(fromDate) || testDate.isAfter(toDate));
    }


    public void deleteReservation(UUID id) throws ReservationNotFoundException {
        if (reservationRepository.findById(id).isEmpty())
            throw new ReservationNotFoundException(id);
        else
            reservationRepository.deleteById(id);

    }
}
