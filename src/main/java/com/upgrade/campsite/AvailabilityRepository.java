package com.upgrade.campsite;

import com.upgrade.campsite.model.AvailabilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity, Long> {

    @Lock(value = LockModeType.OPTIMISTIC)
    Optional<AvailabilityEntity> findByDate(LocalDate date);

    List<AvailabilityEntity> findByDateBetween(
            LocalDate from, LocalDate until);

    /**
     * To get a list of Availability for given dates where capacity for given day is > 0
     *
     * @param from
     * @param todate
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
