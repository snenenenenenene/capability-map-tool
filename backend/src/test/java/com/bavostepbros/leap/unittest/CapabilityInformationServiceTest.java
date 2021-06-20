package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
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

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityInformation;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Information;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.strategicimportance.StrategicImportance;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.capabilityinformationservice.CapabilityInformationService;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.CapabilityInformationDAL;
import com.bavostepbros.leap.persistence.InformationDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class CapabilityInformationServiceTest {
	
	@Autowired
	private CapabilityInformationService capabilityInformationService;
	
	@MockBean
	private CapabilityInformationDAL capabilityInformationDAL;
	
	@MockBean
	private CapabilityDAL capabilityDAL;
	
	@MockBean
	private InformationDAL informationDAL;
	
	private Status statusFirst;
	private Status statusSecond;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Capability capabilityFirst;
	private Capability capabilitySecond;
	private Information informationFirst;
	private Information informationSecond;
	private CapabilityInformation capabilityInformationFirst;
	private CapabilityInformation capabilityInformationSecond;
	private List<CapabilityInformation> capabilityInformations;
	private Optional<Capability> optionalCapabilityFirst;
	private Optional<Information> optionalInformationFirst;
	private Optional<CapabilityInformation> optionalCapabilityInformationFirst;
	
	private Integer capabilityId;
	private Integer informationId;
	private String strategicImportance;
	
	@BeforeEach
	void init() {
		statusFirst = new Status(1, LocalDate.of(2021, 05, 15));
		statusSecond = new Status(2, LocalDate.of(2021, 10, 10));
		environmentFirst = new Environment(1, "Environment test");
		environmentSecond = new Environment(2, "Environment test");
		capabilityFirst = new Capability(1, environmentFirst, statusFirst, 0, "Capability 1", "Description 1",
				PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 9, 8);
		capabilitySecond = new Capability(2, environmentFirst, statusSecond, capabilityFirst.getCapabilityId(),
				"Capability 2", "Description 2", PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 9, 8);
		informationFirst = new Information(1, "Information 1", "Description 1");
		informationSecond = new Information(2, "Information 2", "Description 2");
		capabilityInformationFirst = new CapabilityInformation(capabilityFirst, informationFirst, StrategicImportance.HIGHEST);
		capabilityInformationSecond = new CapabilityInformation(capabilitySecond, informationSecond, StrategicImportance.LOW);
		capabilityInformations = List.of(capabilityInformationFirst, capabilityInformationSecond);
		optionalCapabilityFirst = Optional.of(capabilityFirst);
		optionalInformationFirst = Optional.of(informationFirst);
		optionalCapabilityInformationFirst = Optional.of(capabilityInformationFirst);
		
		capabilityId = capabilityInformationFirst.getCapability().getCapabilityId();
		informationId = capabilityInformationFirst.getInformation().getInformationId();
		strategicImportance = capabilityInformationFirst.getCriticality().toString();
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(capabilityInformationService);
		assertNotNull(capabilityInformationDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(informationDAL);
		
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
		assertNotNull(capabilityFirst);
		assertNotNull(capabilitySecond);
		assertNotNull(informationFirst);
		assertNotNull(informationSecond);	
		assertNotNull(capabilityInformationFirst);
		assertNotNull(capabilityInformationSecond);
		assertNotNull(capabilityInformations);
		assertNotNull(optionalCapabilityFirst);	
		assertNotNull(optionalInformationFirst);
		assertNotNull(optionalCapabilityInformationFirst);
		
		assertNotNull(capabilityId);	
		assertNotNull(informationId);
		assertNotNull(strategicImportance);	
	}
	
	@Test
	void should_returnCapabilityInformation_whenSaveCapabilityInformation() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(informationDAL.findById(BDDMockito.anyInt())).willReturn(optionalInformationFirst);
		BDDMockito.given(capabilityInformationDAL.save(BDDMockito.any(CapabilityInformation.class)))
				.willReturn(capabilityInformationFirst);

		CapabilityInformation capabilityInformation = capabilityInformationService.save(capabilityId, informationId, strategicImportance);
		
		assertNotNull(capabilityInformation);
		assertTrue(capabilityInformation instanceof CapabilityInformation);
		testCapabilityInformation(capabilityInformationFirst, capabilityInformation);
	}
	
	@Test 
	void should_throwNullPointerException_whenGetCapabilityInformationByIdInvalidId() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(informationDAL.findById(BDDMockito.anyInt())).willReturn(optionalInformationFirst);
		String expectedErrorMessage = "CapabilityInformation does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> capabilityInformationService.get(capabilityId, informationId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test
	void should_returnCapabilityInformation_whenGetCapabilityInformationById() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(informationDAL.findById(BDDMockito.anyInt())).willReturn(optionalInformationFirst);
		BDDMockito.given(capabilityInformationDAL.findByCapabilityAndInformation(BDDMockito.any(Capability.class), BDDMockito.any(Information.class)))
				.willReturn(optionalCapabilityInformationFirst);

		CapabilityInformation capabilityInformation = capabilityInformationService.get(capabilityId, informationId);
		
		assertNotNull(capabilityInformation);
		assertTrue(capabilityInformation instanceof CapabilityInformation);
		testCapabilityInformation(capabilityInformationFirst, capabilityInformation);
	}
	
	@Test
	void should_returnCapabilityInformation_whenUpdateCapabilityInformation() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(informationDAL.findById(BDDMockito.anyInt())).willReturn(optionalInformationFirst);
		BDDMockito.given(capabilityInformationDAL.save(BDDMockito.any(CapabilityInformation.class)))
				.willReturn(capabilityInformationFirst);

		CapabilityInformation capabilityInformation = capabilityInformationService.update(capabilityId, informationId, strategicImportance);
		
		assertNotNull(capabilityInformation);
		assertTrue(capabilityInformation instanceof CapabilityInformation);
		testCapabilityInformation(capabilityInformationFirst, capabilityInformation);
	}
	
	@Test
	void should_verifyDeleted_whenDeleteCapabilityInformation() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(informationDAL.findById(BDDMockito.anyInt())).willReturn(optionalInformationFirst);

		capabilityInformationService.delete(capabilityId, informationId);
		
		Mockito.verify(capabilityInformationDAL, Mockito.times(1)).deleteByCapabilityAndInformation(
				BDDMockito.any(Capability.class), BDDMockito.any(Information.class));
	}
	
	@Test
	void should_returnCapabilityInformation_whenGetCapabilityInformationByCapability() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(capabilityInformationDAL.findByCapability(BDDMockito.any(Capability.class)))
				.willReturn(capabilityInformations);

		List<CapabilityInformation> capabilityInformationsResult = capabilityInformationService.getCapabilityInformationByCapability(capabilityId);
		
		assertNotNull(capabilityInformationsResult);
		assertEquals(capabilityInformations.size(), capabilityInformationsResult.size());
		testCapabilityInformation(capabilityInformationFirst, capabilityInformationsResult.get(0));
		testCapabilityInformation(capabilityInformationSecond, capabilityInformationsResult.get(1));
	}
	
	@Test
	void should_returnCapabilityInformation_whenGetCapabilityInformationByInformation() {
		BDDMockito.given(informationDAL.findById(BDDMockito.anyInt())).willReturn(optionalInformationFirst);
		BDDMockito.given(capabilityInformationDAL.findByInformation(BDDMockito.any(Information.class)))
				.willReturn(capabilityInformations);

		List<CapabilityInformation> capabilityInformationsResult = capabilityInformationService.getCapabilityInformationByInformation(informationId);
		
		assertNotNull(capabilityInformationsResult);
		assertEquals(capabilityInformations.size(), capabilityInformationsResult.size());
		testCapabilityInformation(capabilityInformationFirst, capabilityInformationsResult.get(0));
		testCapabilityInformation(capabilityInformationSecond, capabilityInformationsResult.get(1));
	}
	
	private void testCapabilityInformation(CapabilityInformation expectedObject, CapabilityInformation actualObject) {
		assertEquals(expectedObject.getCapability().getCapabilityId(), actualObject.getCapability().getCapabilityId());
		assertEquals(expectedObject.getInformation().getInformationId(), actualObject.getInformation().getInformationId());
		assertEquals(expectedObject.getCriticality(), actualObject.getCriticality());
	}
}
