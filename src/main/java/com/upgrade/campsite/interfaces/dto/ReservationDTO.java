package com.upgrade.campsite.interfaces.dto;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class ReservationDTO {

    private UUID uid;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String name;
    private String email;

    public ReservationDTO() {

    }

    public ReservationDTO(String name, String email, LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.name = name;
        this.email = email;
    }
    /**
     * //      this.uid = UUID.randomUUID(); could be generated here
     */
    public ReservationDTO(UUID uid, String name, String email, LocalDate fromDate, LocalDate toDate) {
        this.uid = uid;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.name = name;
        this.email = email;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    //TODO USE UUID
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationDTO)) return false;
        ReservationDTO that = (ReservationDTO) o;
        return Objects.equals(getUid(), that.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUid());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "uid=" + uid +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    private enum booking_Status {BOOKED, CANCELED, PENDING}

}
