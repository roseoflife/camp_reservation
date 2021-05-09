package com.upgrade.campsite.dao.repositories;

import com.upgrade.campsite.domain.model.AvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity, Long> {

    /**
     * To get a list of Availability for given dates where capacity for given day is > 0
     *
     * @param from : startBookingDate
     * @param todate endBookingDate
     * @return List<Availability>
     */

    default List<AvailabilityEntity> findAvailfromTo(LocalDate from, LocalDate todate) {
        List<AvailabilityEntity> availabilityList = new ArrayList<AvailabilityEntity>();
        findAll().forEach(
                a -> {
                    if (a.getDate().isAfter(from) || a.getDate().isEqual(from) && a.getDate().isBefore(todate) || a.getDate().isEqual(todate))
                        if (a.getCapacity() > 0)
                            availabilityList.add(a);
                }
        );
        return availabilityList;
    }
}
