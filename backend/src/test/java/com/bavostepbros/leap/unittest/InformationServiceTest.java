package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
	
	@Test 
	void should_returnInformation_whenSaveInformation() {
		BDDMockito.given(informationDAL.save(BDDMockito.any(Information.class))).willReturn(informationFirst);
		
		Information information = informationService.save(informationName, informationDescription);
		
		assertNotNull(information);
		assertTrue(information instanceof Information);
		testInformation(informationFirst, information);
	}
	
	@Test 
	void should_throwNullPointerException_whenGetInformationById() {
		String expectedErrorMessage = "Information does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> informationService.get(informationId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnInformation_whenGetInformationById() {
		BDDMockito.given(informationDAL.findById(BDDMockito.anyInt())).willReturn(optionalInformationFirst);
		
		Information information = informationService.get(informationId);
		
		assertNotNull(information);
		assertTrue(information instanceof Information);
		testInformation(informationFirst, information);
	}
	
	@Test 
	void should_returnInformation_whenUpdateInformation() {
		BDDMockito.given(informationDAL.save(BDDMockito.any(Information.class))).willReturn(informationFirst);
		
		Information information = informationService.update(informationId, informationName, informationDescription);
		
		assertNotNull(information);
		assertTrue(information instanceof Information);
		testInformation(informationFirst, information);
	}
	
	@Test 
	void should_verify_whenDeleteInformation() {
		informationService.delete(informationId);
		
		Mockito.verify(informationDAL, Mockito.times(1)).deleteById(Mockito.eq(informationId));
	}
	
	@Test 
	void should_throwNullPointerException_whenGetInformationByName() {
		String expectedErrorMessage = "Information does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> informationService.getInformationByName(informationName));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnInformation_whenGetInformationByName() {
		BDDMockito.given(informationDAL.findByInformationName(BDDMockito.anyString())).willReturn(optionalInformationFirst);
		
		Information information = informationService.getInformationByName(informationName);
		
		assertNotNull(information);
		assertTrue(information instanceof Information);
		testInformation(informationFirst, information);
	}
	
	@Test 
	void should_returnInformations_whenGetAllInformation() {
		BDDMockito.given(informationDAL.findAll()).willReturn(informations);
		
		List<Information> informationsResult = informationService.getAll();
		
		assertNotNull(informationsResult);
		assertEquals(informations.size(), informationsResult.size());
		testInformation(informationFirst, informationsResult.get(0));
		testInformation(informationSecond, informationsResult.get(1));
	}
	
	private void testInformation(Information expectedObject, Information actualObject) {
		assertEquals(expectedObject.getInformationId(), actualObject.getInformationId());
		assertEquals(expectedObject.getInformationName(), actualObject.getInformationName());
		assertEquals(expectedObject.getInformationDescription(), actualObject.getInformationDescription());
	}
}
