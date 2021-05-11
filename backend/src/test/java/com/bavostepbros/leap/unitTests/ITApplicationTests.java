package com.bavostepbros.leap.unitTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.service.itapplicationService.ITApplicationService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ITApplicationTests {

    @Autowired
    private ITApplicationService itApplicationService;

    private ITApplication itApplication;

    @Before
    public void init(){
        System.out.println("ITAPPLICATION TESTS STARTED\n");
        itApplication = Mockito.mock(ITApplication.class);
    }

    @Test
    public void saveITApplicationSuccess(){
        System.out.println("STARTED SAVE TESTING\n");
        ITApplication savedITApplication = itApplicationService.save(itApplication);
        System.out.println("itapplication: " + itApplication);
        assertTrue(savedITApplication.equals(itApplication));
    }
}
