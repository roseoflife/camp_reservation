package com.upgrade.campsite;

import com.upgrade.campsite.application.ReservationController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ReservationControllerTest {

    @Autowired
    protected WebApplicationContext wac;
    // @Autowired
    MockMvc mockMvc;
    @Autowired
    ReservationController resController;

    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(this.resController).build();
    }

    @Test
    public void testReservations() throws Exception {
        mockMvc.perform(get("/reservations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}



