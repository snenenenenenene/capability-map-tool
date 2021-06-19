package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.bavostepbros.leap.domain.model.Resource;
import com.bavostepbros.leap.domain.service.resourceservice.ResourceService;
import com.bavostepbros.leap.persistence.ResourceDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class ResourceServiceTest {
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private ResourceDAL resourceDAL;
	
	private Resource resourceFirst;
	private Resource resourceSecond;
	
	private String resourceName;
	private String resourceDescription;
	private Double fullTimeEquivalentYearlyValue;
	
	@BeforeEach
	void init() {
		resourceFirst = new Resource(1, "Resource 1", "Good description", 20.0);
		resourceSecond = new Resource(2, "Resource 2", "Good description 2", 40.0);
		
		resourceName = resourceFirst.getResourceName();
		resourceDescription = resourceFirst.getResourceDescription();
		fullTimeEquivalentYearlyValue = resourceFirst.getFullTimeEquivalentYearlyValue();		
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(resourceService);
		assertNotNull(resourceDAL);
		assertNotNull(resourceFirst);
		assertNotNull(resourceSecond);
	}
}
