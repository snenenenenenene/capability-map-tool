package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.domain.service.technologyservice.TechnologyService;
import com.bavostepbros.leap.persistence.TechnologyDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class TechnologyServiceTest {
	
	@Autowired
	private TechnologyService technologyService;
	
	@MockBean
	private TechnologyDAL technologyDAL;
	
	private Technology technologyFirst;
	private Technology technologySecond;
	private List<Technology> technologies;
	private Optional<Technology> optionalTechnologyFirst;
	
	private Integer technologyId;
	private String technologyName;
	
	@BeforeEach
	void init() {
		technologyFirst = new Technology(1, "Java");
		technologySecond = new Technology(2, "Javascript");
		technologies = List.of(technologyFirst, technologySecond);
		optionalTechnologyFirst = Optional.of(technologyFirst);
		
		technologyId = technologyFirst.getTechnologyId();
		technologyName = technologyFirst.getTechnologyName();
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(technologyService);
		assertNotNull(technologyDAL);
		
		assertNotNull(technologyFirst);
		assertNotNull(technologySecond);
		assertNotNull(technologies);
		assertNotNull(optionalTechnologyFirst);
		
		assertNotNull(technologyId);
		assertNotNull(technologyName);
	}
	
	@Test 
	void should_returnTechnology_whenSaveTechnology() {
		BDDMockito.given(technologyDAL.save(BDDMockito.any(Technology.class))).willReturn(technologyFirst);
		
		Technology technology = technologyService.save(technologyName);
		
		assertNotNull(technology);
		assertTrue(technology instanceof Technology);
		testTechnology(technologyFirst, technology);
	}
	
	@Test 
	void should_throwNullPointerException_whenGetTechnologyByIdInvalidId() {
		String expectedErrorMessage = "Technology does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> technologyService.get(technologyId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnTechnology_whenGetTechnologyById() {
		BDDMockito.given(technologyDAL.findById(BDDMockito.anyInt())).willReturn(optionalTechnologyFirst);
		
		Technology technology = technologyService.get(technologyId);
		
		assertNotNull(technology);
		assertTrue(technology instanceof Technology);
		testTechnology(technologyFirst, technology);
	}
	
	@Test 
	void should_returnTechnology_whenUpdateTechnology() {
		BDDMockito.given(technologyDAL.save(BDDMockito.any(Technology.class))).willReturn(technologyFirst);
		
		Technology technology = technologyService.update(technologyId, technologyName);
		
		assertNotNull(technology);
		assertTrue(technology instanceof Technology);
		testTechnology(technologyFirst, technology);
	}
	
	@Test 
	void should_verifyDeleted_whenDeleteTechnology() {
		technologyService.delete(technologyId);
		
		Mockito.verify(technologyDAL, Mockito.times(1)).deleteById(Mockito.eq(technologyId));
	}
	
	@Test 
	void should_throwNullPointerException_whenGetTechnologyByNameInvalidName() {
		String expectedErrorMessage = "Technology does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> technologyService.getByTechnologyName(technologyName));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnTechnology_whenGetTechnologyByName() {
		BDDMockito.given(technologyDAL.findByTechnologyName(BDDMockito.anyString())).willReturn(optionalTechnologyFirst);
		
		Technology technology = technologyService.getByTechnologyName(technologyName);
		
		assertNotNull(technology);
		assertTrue(technology instanceof Technology);
		testTechnology(technologyFirst, technology);
	}
	
	@Test 
	void should_returnTechnology_whenGetAllTechnology() {
		BDDMockito.given(technologyDAL.findAll()).willReturn(technologies);
		
		List<Technology> technologiesResult = technologyService.getAll();
		
		assertNotNull(technologiesResult);
		assertEquals(technologies.size(), technologiesResult.size());
		testTechnology(technologyFirst, technologiesResult.get(0));
		testTechnology(technologySecond, technologiesResult.get(1));
	}
	
	@Test 
	void should_ReturnFalse_whenTechnologyDoesNotExistById() {
		BDDMockito.given(technologyDAL.existsById(BDDMockito.anyInt())).willReturn(false);

		boolean result = technologyService.existsById(technologyId);

		assertFalse(result);
	}
	
	@Test 
	void should_ReturnTrue_whenTechnologyDoesExistById() {
		BDDMockito.given(technologyDAL.existsById(BDDMockito.anyInt())).willReturn(true);

		boolean result = technologyService.existsById(technologyId);

		assertTrue(result);
	}
	
	@Test 
	void should_ReturnFalse_whenTechnologyDoesNotExistByName() {
		BDDMockito.given(technologyDAL.findByTechnologyName(BDDMockito.anyString())).willReturn(Optional.empty());

		boolean result = technologyService.existsByTechnologyName(technologyName);

		assertFalse(result);
	}
	
	@Test 
	void should_ReturnTrue_whenTechnologyDoesExistByName() {
		BDDMockito.given(technologyDAL.findByTechnologyName(BDDMockito.anyString())).willReturn(optionalTechnologyFirst);

		boolean result = technologyService.existsByTechnologyName(technologyName);

		assertTrue(result);
	}
	
	private void testTechnology(Technology expectedObject, Technology actualObject) {
		assertEquals(expectedObject.getTechnologyId(), actualObject.getTechnologyId());
		assertEquals(expectedObject.getTechnologyName(), actualObject.getTechnologyName());
	}
}
