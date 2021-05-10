package com.upgrade.campsite.application;

import com.upgrade.campsite.domain.model.exceptions.InvalidDatesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DataValidator {
    private final Logger log = LoggerFactory.getLogger(DataValidator.class);

    public Boolean validateDates(LocalDate fromDate, LocalDate toDate) throws IllegalArgumentException {
        if (fromDate == null) {
            log.error("start date is null");
            throw new InvalidDatesException(fromDate, toDate, "Please choose a start date for your reservation");
        }
        if (toDate == null) {
            log.error("end date is null");
            throw new InvalidDatesException(fromDate, toDate, "Please choose an end date for your reservation");
        }
        if (fromDate.isEqual(toDate)) {
            log.error("Reservation should be at least for one day");
            throw new InvalidDatesException(fromDate, toDate, "Reservation should be at least for one day");
        } else if (fromDate.isAfter(toDate)) {
            log.error("Departure date should be after arrival Date");
            throw new InvalidDatesException(fromDate, toDate, "Departure date should be after arrival Date");
        } else if (toDate.isAfter(fromDate.plusDays(3))) {
            log.error("Reservation is for maximum 3 days");
            throw new InvalidDatesException(fromDate, toDate, "Reservation is for maximum 3 days");
        } else if (toDate.isAfter(toDate.plusDays(30))) {
            log.error("Reservation is up to 1 month in advance");
            throw new InvalidDatesException(fromDate, toDate, "Reservation is up to 1 month in advance");
        } else
            return true;
    }
}
