package com.bavostepbros.leap.unittest;

import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.service.itapplicationservice.ITApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// ... superklasse
// extends itapplication service?

@AutoConfigureMockMvc
@SpringBootTest
class ITApplicationServiceTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private ITApplicationService itApplicationService;

    private ITApplication itApplication;

    @BeforeEach
    void init() {
        System.out.println("initializing tests...");
//        itApplication = new ITApplication(new Status(LocalDate.of(2004, 4, 4)),
//                "pup", "pup",
//                LocalDate.of(2222, 2, 2), LocalDate.of(3333, 3, 3),
//                1, 2, 3, 4, 4, 3, 2, 1, "pup", "pup",
//                77.765, LocalDate.of(4444, 4, 4));


    }


    @Test
    void shouldNotBeNull() {
        assertNotNull(itApplicationService);
    }


	/*
	 * @Test void save() { ITApplication savedITApplication =
	 * itApplicationService.save(itApplication); assertNotNull(savedITApplication);
	 * }
	 */

}