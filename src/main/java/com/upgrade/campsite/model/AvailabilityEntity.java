package com.upgrade.campsite.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class AvailabilityEntity {

    private @Id
    @GeneratedValue
    Long uid;
    private LocalDate date;
    private int capacity;

    public AvailabilityEntity(Long uid, LocalDate date, int capacity) {
        this.uid = uid;
        this.date = date;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Temporal(TemporalType.DATE)
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Availability{" +
                "uid=" + uid +
                ", date=" + date +
                '}';
    }

}
