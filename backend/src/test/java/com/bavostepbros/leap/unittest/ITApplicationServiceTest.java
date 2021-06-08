package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.domain.model.timevalue.TimeValue;
import com.bavostepbros.leap.domain.service.itapplicationservice.ITApplicationService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.ITApplicationDAL;
import com.bavostepbros.leap.persistence.StatusDAL;

@AutoConfigureMockMvc
@SpringBootTest
class ITApplicationServiceTest {

	@SuppressWarnings("unused")
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ITApplicationService itApplicationService;

	@MockBean
	private StatusDAL statusDAL;

	@MockBean
	private ITApplicationDAL applicationDAL;

	@SpyBean
	private StatusService spyStatusService;

	@SpyBean
	private ITApplicationService spyITApplicationService;

	private Status statusFirst;
	private Status statusSecond;
	private Technology technologyFirst;
	private Technology technologySecond;
	private ITApplication itApplicationFirst;
	private ITApplication itApplicationSecond;
	private List<ITApplication> itApplications;
	private Optional<Status> optionalStatusFirst;
	private Optional<ITApplication> optionalITApplicationFirst;

	private Integer itApplicationId;
	private Integer statusId;
	private String applicationName;
	private String version;
	private LocalDate purchaseDate;
	private LocalDate endOfLife;
	private Integer currentScalability;
	private Integer expectedScalability;
	private Integer currentPerformance;
	private Integer expectedPerformance;
	private Integer currentSecurityLevel;
	private Integer expectedSecurityLevel;
	private Integer currentStability;
	private Integer expectedStability;
	private String currencyType;
	private Double costCurrency;
	private Integer currentValue;
	private Double currentYearlyCost;
	private Double acceptedYearlyCost;
	private String timeValue;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@BeforeEach
	void init() {
		statusFirst = new Status(1, LocalDate.of(2021, 05, 15));
		statusSecond = new Status(2, LocalDate.of(2021, 10, 10));
		technologyFirst = new Technology(1, "Java");
		technologySecond = new Technology(1, "c#");
		itApplicationFirst = new ITApplication(1, statusFirst, "application 1", "1.20.1", LocalDate.of(2021, 01, 20),
				LocalDate.of(2025, 05, 20), 1, 2, 3, 4, 5, 4, 3, 2, "EUR", 1000.0, 5, 70.0, 100.0, TimeValue.ELIMINATE);
		itApplicationSecond = new ITApplication(2, statusSecond, "application 2", "1.20.1", LocalDate.of(2021, 01, 20),
				LocalDate.of(2025, 05, 20), 2, 3, 4, 5, 4, 3, 2, 1, "EUR", 1000.0, 4, 70.0, 100.0, TimeValue.INVEST);
		itApplications = List.of(itApplicationFirst, itApplicationSecond);
		optionalStatusFirst = Optional.of(statusFirst);
		optionalITApplicationFirst = Optional.of(itApplicationFirst);

		itApplicationId = itApplicationFirst.getItApplicationId();
		statusId = itApplicationFirst.getStatus().getStatusId();
		applicationName = itApplicationFirst.getName();
		version = itApplicationFirst.getVersion();
		purchaseDate = itApplicationFirst.getPurchaseDate();
		endOfLife = itApplicationFirst.getEndOfLife();
		currentScalability = itApplicationFirst.getCurrentScalability();
		expectedScalability = itApplicationFirst.getExpectedScalability();
		currentPerformance = itApplicationFirst.getCurrentPerformance();
		expectedPerformance = itApplicationFirst.getExpectedPerformance();
		currentSecurityLevel = itApplicationFirst.getCurrentSecurityLevel();
		expectedSecurityLevel = itApplicationFirst.getExpectedSecurityLevel();
		currentStability = itApplicationFirst.getCurrentStability();
		expectedStability = itApplicationFirst.getExpectedStability();
		currencyType = itApplicationFirst.getCurrencyType();
		costCurrency = itApplicationFirst.getCostCurrency();
		currentValue = itApplicationFirst.getCurrentValue();
		currentYearlyCost = itApplicationFirst.getCurrentYearlyCost();
		acceptedYearlyCost = itApplicationFirst.getAcceptedYearlyCost();
		timeValue = itApplicationFirst.getTimeValue().toString();
	}

	@Test
	void shouldNotBeNull() {
		assertNotNull(itApplicationService);
		assertNotNull(statusDAL);
		assertNotNull(applicationDAL);
		assertNotNull(spyStatusService);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(technologyFirst);
		assertNotNull(technologySecond);
		assertNotNull(itApplicationFirst);
		assertNotNull(itApplicationSecond);
		assertNotNull(itApplications);
		assertNotNull(optionalStatusFirst);
		assertNotNull(optionalITApplicationFirst);
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputNameIsInvalid() {
		String applicationName = "";
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputNameIsDuplicate() {
		String applicationName = itApplicationFirst.getName();
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputVersionIsInvalid() {
		String version = "";
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputPurchaseDateIsInvalid() {
		LocalDate purchaseDate = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputEndOfLifeIsInvalid() {
		LocalDate endOfLife = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentScalabilityIsNull() {
		Integer currentScalability = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentScalabilityIsSmallerThan1() {
		Integer currentScalability = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentScalabilityIsGreaterThan5() {
		Integer currentScalability = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectedScalabilityIsNull() {
		Integer expectedScalability = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectedScalabilityIsSmallerThan1() {
		Integer expectedScalability = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectedScalabilityIsSmallerGreaterThan5() {
		Integer expectedScalability = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentPerformanceIsNull() {
		Integer currentPerformance = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentPerformanceIsSmallerThan1() {
		Integer currentPerformance = 1;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentPerformanceIsGreaterThan5() {
		Integer currentPerformance = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectederformanceIsNull() {
		Integer expectedPerformance = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectederformanceIsSmallerThan1() {
		Integer expectedPerformance = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectederformanceIsGreaterThan5() {
		Integer expectedPerformance = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentSecurityLevelIsNull() {
		Integer currentSecurityLevel = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentSecurityLevelIsSmallerThan1() {
		Integer currentSecurityLevel = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentSecurityLevelIsGreaterThan5() {
		Integer currentSecurityLevel = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectedSecurityLevelIsNull() {
		Integer expectedSecurityLevel = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectedSecurityLevelIsSmallerThan1() {
		Integer expectedSecurityLevel = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectedSecurityLevelIsGreaterThan5() {
		Integer expectedSecurityLevel = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentStabilityIsNull() {
		Integer currentStability = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentStabilityIsSmallerThan1() {
		Integer currentStability = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentStabilityIsGreaterThan5() {
		Integer currentStability = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectedStabilityIsNull() {
		Integer expectedStability = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectedStabilityIsSmallerThan1() {
		Integer expectedStability = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputExpectedStabilityIsGreaterThan5() {
		Integer expectedStability = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentValueIsNull() {
		Integer currentValue = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentValueIsSmallerThan1() {
		Integer currentValue = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentValueIsGreaterThan5() {
		Integer currentValue = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputCurrentYearlyCostIsNull() {
		Double currentYearlyCost = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputAcceptedYearlyCostIsNull() {
		Double acceptedYearlyCost = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenSavedInputTimeValueIsNull() {
		String timeValue = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.save(statusId, applicationName, version, purchaseDate, endOfLife,
						currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_returnITApplication_whenSaved() {
		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatusFirst);
		BDDMockito.given(applicationDAL.save(BDDMockito.any(ITApplication.class))).willReturn(itApplicationFirst);

		ITApplication application = itApplicationService.save(statusId, applicationName, version, purchaseDate,
				endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
				currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
				costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue);

		assertNotNull(application);
		assertTrue(application instanceof ITApplication);
		testItApplication(itApplicationFirst, application);
	}

	@Test
	void should_throwNoSuchElementException_whenGetITApplicationIdIsNull() {
		Integer itApplicationId = null;
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.get(itApplicationId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenGetITApplicationIdIsInvalid() {
		Integer itApplicationId = 0;
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.get(itApplicationId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_returnITApplication_whenGetITApplication() {
		BDDMockito.given(applicationDAL.findById(itApplicationId)).willReturn(optionalITApplicationFirst);

		ITApplication application = itApplicationService.get(itApplicationId);

		assertNotNull(application);
		assertTrue(application instanceof ITApplication);
		testItApplication(itApplicationFirst, application);
	}

	@Test
	void should_throwNoSuchElementException_whenUpdatedInputIdIsNull() {
		Integer itApplicationId = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdatedInputIdIsInvalid() {
		Integer itApplicationId = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdatedInputNameIsInvalid() {
		String applicationName = "";
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputNameIsDuplicate() {
		String applicationName = itApplicationFirst.getName();
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputVersionIsInvalid() {
		String version = "";
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputPurchaseDateIsInvalid() {
		LocalDate purchaseDate = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputEndOfLifeIsInvalid() {
		LocalDate endOfLife = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentScalabilityIsNull() {
		Integer currentScalability = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentScalabilityIsSmallerThan1() {
		Integer currentScalability = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentScalabilityIsGreaterThan5() {
		Integer currentScalability = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectedScalabilityIsNull() {
		Integer expectedScalability = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectedScalabilityIsSmallerThan1() {
		Integer expectedScalability = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectedScalabilityIsSmallerGreaterThan5() {
		Integer expectedScalability = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentPerformanceIsNull() {
		Integer currentPerformance = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentPerformanceIsSmallerThan1() {
		Integer currentPerformance = 1;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentPerformanceIsGreaterThan5() {
		Integer currentPerformance = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectederformanceIsNull() {
		Integer expectedPerformance = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectederformanceIsSmallerThan1() {
		Integer expectedPerformance = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectederformanceIsGreaterThan5() {
		Integer expectedPerformance = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentSecurityLevelIsNull() {
		Integer currentSecurityLevel = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentSecurityLevelIsSmallerThan1() {
		Integer currentSecurityLevel = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentSecurityLevelIsGreaterThan5() {
		Integer currentSecurityLevel = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectedSecurityLevelIsNull() {
		Integer expectedSecurityLevel = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectedSecurityLevelIsSmallerThan1() {
		Integer expectedSecurityLevel = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectedSecurityLevelIsGreaterThan5() {
		Integer expectedSecurityLevel = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentStabilityIsNull() {
		Integer currentStability = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentStabilityIsSmallerThan1() {
		Integer currentStability = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentStabilityIsGreaterThan5() {
		Integer currentStability = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectedStabilityIsNull() {
		Integer expectedStability = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectedStabilityIsSmallerThan1() {
		Integer expectedStability = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputExpectedStabilityIsGreaterThan5() {
		Integer expectedStability = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentValueIsNull() {
		Integer currentValue = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentValueIsSmallerThan1() {
		Integer currentValue = 0;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentValueIsGreaterThan5() {
		Integer currentValue = 6;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputCurrentYearlyCostIsNull() {
		Double currentYearlyCost = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputAcceptedYearlyCostIsNull() {
		Double acceptedYearlyCost = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwNoSuchElementException_whenUpdateInputTimeValueIsNull() {
		String timeValue = null;
		String expected = "No value present";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> itApplicationService.update(itApplicationId, statusId, applicationName, version, purchaseDate,
						endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance,
						currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, currencyType,
						costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_returnITApplication_whenUpdated() {
		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);

		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatusFirst);
		BDDMockito.given(applicationDAL.save(BDDMockito.any(ITApplication.class))).willReturn(itApplicationFirst);

		ITApplication application = itApplicationService.update(itApplicationId, statusId, applicationName, version,
				purchaseDate, endOfLife, currentScalability, expectedScalability, currentPerformance,
				expectedPerformance, currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability,
				currencyType, costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue);

		assertNotNull(application);
		assertTrue(application instanceof ITApplication);
		testItApplication(itApplicationFirst, application);
	}

	@Test
	void should_returnTrue_whenDeleted() {
		itApplicationService.delete(itApplicationId);

		Mockito.verify(applicationDAL, Mockito.times(1)).deleteById(Mockito.eq(itApplicationId));
	}

	@Test
	void should_ReturnFalse_whenStrategyDoesNotExistById() {
		BDDMockito.given(applicationDAL.existsById(BDDMockito.anyInt())).willReturn(false);

		boolean result = itApplicationService.existsById(itApplicationId);

		assertFalse(result);
	}

	@Test
	void should_ReturnTrue_whenStrategyDoesExistById() {
		BDDMockito.given(applicationDAL.existsById(BDDMockito.anyInt())).willReturn(true);

		boolean result = itApplicationService.existsById(itApplicationId);

		assertTrue(result);
	}

	@Test
	void should_throwNoSuchElementException_whenGetByNameITApplicationNameIsNull() {
		String applicationName = null;
		String expected = "IT-application does not exist.";

		Exception exception = assertThrows(NullPointerException.class,
				() -> itApplicationService.getItApplicationByName(applicationName));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_returnITApplication_whenGetByNameITApplication() {
		BDDMockito.given(applicationDAL.findByName(applicationName)).willReturn(optionalITApplicationFirst);

		ITApplication application = itApplicationService.getItApplicationByName(applicationName);

		assertNotNull(application);
		assertTrue(application instanceof ITApplication);
		testItApplication(itApplicationFirst, application);
	}

	@Test
	void should_returnITApplications_whenGetAll() {
		BDDMockito.given(applicationDAL.findAll()).willReturn(itApplications);

		List<ITApplication> fetchedApplications = itApplicationService.getAll();

		assertNotNull(fetchedApplications);
		assertEquals(itApplications.size(), fetchedApplications.size());
	}

	@Test
	void should_returnCurrencies_whenGetAllCurrencies() {
		List<String> currencies = Currency.getAvailableCurrencies().stream()
				.map(currency -> currency.getCurrencyCode())
				.collect(Collectors.toList());

		BDDMockito.doReturn(currencies).when(spyITApplicationService).getAllCurrencies();

		List<String> fetchedCurrencies = itApplicationService.getAllCurrencies();

		assertNotNull(fetchedCurrencies);
		assertEquals(currencies.size(), fetchedCurrencies.size());
	}

	@Test
	void should_returnTimeValues_whenGetAllCurrencies() {
		List<String> timeValues = Arrays.stream(TimeValue.values())
				.map(TimeValue::name)
				.collect(Collectors.toList());
		
		BDDMockito.doReturn(timeValues).when(spyITApplicationService).getAllTimeValues();

		List<String> fetchedTimeValues = itApplicationService.getAllTimeValues();

		assertNotNull(fetchedTimeValues);
		assertEquals(timeValues.size(), fetchedTimeValues.size());
	}

	@Test
	private void testItApplication(ITApplication expectedObject, ITApplication actualObject) {
		assertEquals(expectedObject.getItApplicationId(), actualObject.getItApplicationId());
		assertEquals(expectedObject.getStatus().getStatusId(), actualObject.getStatus().getStatusId());
		assertEquals(expectedObject.getName(), actualObject.getName());
		assertEquals(expectedObject.getStatus().getValidityPeriod(), actualObject.getStatus().getValidityPeriod());
		assertEquals(expectedObject.getVersion(), actualObject.getVersion());
		assertEquals(expectedObject.getPurchaseDate(), actualObject.getPurchaseDate());
		assertEquals(expectedObject.getEndOfLife(), actualObject.getEndOfLife());
		assertEquals(expectedObject.getCurrentScalability(), actualObject.getCurrentScalability());
		assertEquals(expectedObject.getExpectedScalability(), actualObject.getExpectedScalability());
		assertEquals(expectedObject.getCurrentPerformance(), actualObject.getCurrentPerformance());
		assertEquals(expectedObject.getExpectedPerformance(), actualObject.getExpectedPerformance());
		assertEquals(expectedObject.getCurrentSecurityLevel(), actualObject.getCurrentSecurityLevel());
		assertEquals(expectedObject.getExpectedSecurityLevel(), actualObject.getExpectedSecurityLevel());
		assertEquals(expectedObject.getCurrentStability(), actualObject.getCurrentStability());
		assertEquals(expectedObject.getExpectedStability(), actualObject.getExpectedStability());
		assertEquals(expectedObject.getCurrencyType(), actualObject.getCurrencyType());
		assertEquals(expectedObject.getCostCurrency(), actualObject.getCostCurrency());
		assertEquals(expectedObject.getCurrentValue(), actualObject.getCurrentValue());
		assertEquals(expectedObject.getCurrentYearlyCost(), actualObject.getCurrentYearlyCost());
		assertEquals(expectedObject.getAcceptedYearlyCost(), actualObject.getAcceptedYearlyCost());
		assertEquals(expectedObject.getTimeValue(), actualObject.getTimeValue());
	}

}