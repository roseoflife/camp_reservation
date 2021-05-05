package com.upgrade.campsite.Exceptions;

import java.util.UUID;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(UUID id) {
        super("Could not find reservation " + id);
    }
}

