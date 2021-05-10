package com.upgrade.campsite.application;


import com.upgrade.campsite.application.converter.ObjectMapper;
import com.upgrade.campsite.dao.repositories.AvailabilityRepository;
import com.upgrade.campsite.dao.repositories.ReservationRepository;
import com.upgrade.campsite.domain.model.AvailabilityEntity;
import com.upgrade.campsite.domain.model.ReservationEntity;
import com.upgrade.campsite.domain.model.exceptions.InvalidDatesException;
import com.upgrade.campsite.domain.model.exceptions.ReservationFailedException;
import com.upgrade.campsite.domain.model.exceptions.ReservationNotFoundException;
import com.upgrade.campsite.interfaces.dto.ReservationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class ReservationService {

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);
    ObjectMapper mapper = new ObjectMapper();
    AvailabilityRepository availabilityRepository;
    ReservationRepository reservationRepository;
    DataValidator dataValidator = new DataValidator();

    ReservationService(AvailabilityRepository availabilityRepository, ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        this.availabilityRepository = availabilityRepository;

    }

    /**
     * We dont need to lock reservation Table since everyUser has a UUID and does not need to update the same Row.
     * we need to lock Availability to avoid a deadlock sitation. two users updating the same row of database.
     * we could also use @Transactional but it locks everything and affect the performance of the application
     * 2- We use 2 databases one for read one for write (CQRS). DBs needs to be synced
     */

    public ReservationDTO reserveCampfromTo(final ReservationDTO r) throws InvalidDatesException, ReservationFailedException {
        if (updateAvailability(r)) {
            //todo: Use an EntityMapper Here
            ReservationEntity reservation = new ReservationEntity(r.getUid(), r.getName(), r.getEmail(), r.getFromDate(), r.getToDate());
            reservationRepository.save(reservation);
            reservationRepository.flush();
            return r;
        } else
            throw new ReservationFailedException();
    }

    /**
     * Batch update / so if one fails, it rolls back
     * Lock Availability table to avoid deadlock
     * PESSIMISTIC_WRITE lock guarantees that besides dirty and non-repeatable reads are impossible you can update data without obtaining additional locks(and possible deadlocks while waiting for exclusive lock).
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    public boolean updateAvailability(final ReservationDTO r) throws InvalidDatesException, ReservationFailedException {
        List<AvailabilityEntity> availabilities;
        if (dataValidator.validateDates(r.getFromDate(), r.getToDate())) {
            availabilities = availabilityRepository.findAvailfromTo(
                    r.getFromDate(), r.getToDate());
            if (availabilities.isEmpty())
                return false;
            else
                for (AvailabilityEntity availability : availabilities) {
                    availability.setCapacity(availability.getCapacity() - 1);
                }
            availabilityRepository.saveAll(availabilities);
            availabilityRepository.flush();
            return true;
        } else
            throw new ReservationFailedException();
    }

    /**
     * find the exiting reservation and update it.
     * This row could be locked if a user wants to update a row from two different devices. Otherwise, no need for locking.
     *
     * @param newReservation
     * @param id
     * @return
     * @throws InvalidDatesException
     */
    @Transactional
    public ReservationDTO updateReservation(UUID id, ReservationDTO newReservation) throws InvalidDatesException, ReservationNotFoundException {
        if (!reservationRepository.findById(id).isPresent())
            throw new ReservationNotFoundException(id);
        else {
            //first validate the new reservationDates
            List<AvailabilityEntity> availabilities;
            if (dataValidator.validateDates(newReservation.getFromDate(), newReservation.getToDate())) {
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
        if (reservationRepository.findById(id).isEmpty()) {
            log.error("Reservation Not Found for id" + id);
            throw new ReservationNotFoundException(id);
        } else
            reservationRepository.deleteById(id);

    }

    public List<ReservationEntity> all() {
        return reservationRepository.findAll();
    }

    public List<AvailabilityEntity> allAvailabilities() {
        return availabilityRepository.findAll();
    }

    public List<AvailabilityEntity> availabilities(LocalDate fromDate, LocalDate toDate) {
        return availabilityRepository.findAvailfromTo(fromDate, toDate);
    }
}
