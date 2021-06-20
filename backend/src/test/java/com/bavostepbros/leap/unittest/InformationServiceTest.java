package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Information;
import com.bavostepbros.leap.domain.service.informationservice.InformationService;
import com.bavostepbros.leap.persistence.InformationDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class InformationServiceTest {
	
	@Autowired
	private InformationService informationService;
	
	@MockBean
	private InformationDAL informationDAL;
	
	private Information informationFirst;
	private Information informationSecond;
	private List<Information> informations;
	private Optional<Information> optionalInformationFirst;
	
	private Integer informationId;
	private String informationName;
	private String informationDescription;
	
	@BeforeEach
	void init() {
		informationFirst = new Information(1, "Information 1", "Description 1");
		informationSecond = new Information(2, "Information 2", "Description 2");
		informations = List.of(informationFirst, informationSecond);
		optionalInformationFirst = Optional.of(informationFirst);
		
		informationId = informationFirst.getInformationId();
		informationName = informationFirst.getInformationName();
		informationDescription = informationFirst.getInformationDescription();
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(informationService);
		assertNotNull(informationDAL);
		assertNotNull(informationFirst);
		assertNotNull(informationSecond);	
		assertNotNull(informations);
		assertNotNull(optionalInformationFirst);
		assertNotNull(informationId);	
		assertNotNull(informationName);
		assertNotNull(informationDescription);
	}
	
	private void testInformation(Information expectedObject, Information actualObject) {
		assertEquals(expectedObject.getInformationId(), actualObject.getInformationId());
		assertEquals(expectedObject.getInformationName(), actualObject.getInformationName());
		assertEquals(expectedObject.getInformationDescription(), actualObject.getInformationDescription());
	}
}
