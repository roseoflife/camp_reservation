package com.upgrade.campsite.domain.model.exceptions;

import java.time.LocalDate;

public class InvalidDatesException extends RuntimeException {

    public InvalidDatesException(LocalDate fromDate, LocalDate toDate, String message) {
        super(message + " ArrivalTime:" + fromDate + "Departure Time: "+ toDate);
        }
    }

