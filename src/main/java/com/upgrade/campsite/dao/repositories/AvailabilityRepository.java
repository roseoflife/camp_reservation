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
     * //default is one month
     * @param fromDate : startBookingDate
     * @param toDate endBookingDate
     * @return List<Availability>
     */


    default List<AvailabilityEntity> findAvailfromTo(LocalDate fromDate, LocalDate toDate) {
        List<AvailabilityEntity> availabilityList = new ArrayList<AvailabilityEntity>();
        if (toDate == null || fromDate == toDate)
            toDate = LocalDate.now().plusDays(30);
        if (fromDate == null)
            fromDate = LocalDate.now();
        else {
            LocalDate finalFromDate = fromDate;
            LocalDate finalToDate = toDate;
            findAll().forEach(
                    a -> {
                        if ((a.getDate().isAfter(finalFromDate) || a.getDate().isEqual(finalFromDate)) && (a.getDate().isBefore(finalToDate) || a.getDate().isEqual(finalToDate)) && a.getCapacity() > 0)
                            availabilityList.add(a);
                    }
            );
        }
        return availabilityList;
    }
}
