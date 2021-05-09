package com.upgrade.campsite;

import com.upgrade.campsite.application.ReservationService;
import com.upgrade.campsite.domain.model.exceptions.InvalidDatesException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Test
    public void testInvalidateDates() {
        Exception exception = assertThrows(InvalidDatesException.class, () -> {
            Boolean validationStatus = reservationService.validateDates(LocalDate.now(), LocalDate.now().minusDays(4));
        });
        String expectedMessage = "Departure date should be after arrival Date";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testvalidDates() {
        Boolean validationStatus = reservationService.validateDates(LocalDate.now(), LocalDate.now().plusDays(3));
        assertThat(validationStatus).isEqualTo(true);
    }

}