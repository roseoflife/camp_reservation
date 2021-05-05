package com.upgrade.campsite.Exceptions;

import java.time.LocalDate;
import java.util.UUID;

public class InvalidDatesException extends RuntimeException {

    public InvalidDatesException(LocalDate fromDate, LocalDate toDate, String message) {
        super(message + " ArrivalTime:" + fromDate + "Departure Time: "+ toDate);
        }
    }

