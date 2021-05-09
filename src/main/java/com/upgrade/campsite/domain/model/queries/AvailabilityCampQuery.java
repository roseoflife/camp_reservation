package com.upgrade.campsite.domain.model.queries;

import java.time.LocalDate;

public class AvailabilityCampQuery { // no any setters !

    private LocalDate fromDate;
    private LocalDate toDate;

    public AvailabilityCampQuery(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }
}
