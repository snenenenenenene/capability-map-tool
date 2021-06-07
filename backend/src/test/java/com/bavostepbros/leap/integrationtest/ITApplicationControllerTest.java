package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.domain.model.dto.ITApplicationDto;
import com.bavostepbros.leap.domain.model.dto.TechnologyDto;
import com.bavostepbros.leap.domain.model.timevalue.TimeValue;
import com.bavostepbros.leap.domain.service.itapplicationservice.ITApplicationService;
import com.bavostepbros.leap.persistence.ITApplicationDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.bavostepbros.leap.persistence.TechnologyDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ITApplicationControllerTest extends ApiIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private StatusDAL statusDAL;
	
	@Autowired
	private TechnologyDAL technologyDAL;
	
	@Autowired
	private ITApplicationDAL itApplicationDAL;
	
	@Autowired
	private ITApplicationService itApplicationService;
	
	static final String PATH = "/api/itapplication/";
	
	private Status statusFirst;
	private Status statusSecond;
	private Technology technologyFirst;
	private Technology technologySecond;
	private ITApplication itApplicationFirst;
	private ITApplication itApplicationSecond;

	@BeforeAll
	public void authenticate() throws Exception { super.authenticate(); }

	@BeforeEach
	public void init() {
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 05, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 05, 20)));
		technologyFirst = technologyDAL.save(new Technology(1, "Java"));
		technologySecond = technologyDAL.save(new Technology(2, "c#"));
		itApplicationFirst = itApplicationDAL.save(new ITApplication(1, statusFirst, "application 1", 
				"1.20.1", LocalDate.of(2021, 01, 20), LocalDate.of(2025, 05, 20), 1, 2, 3, 4, 5, 
				6, 7, 8, "EUR", 1000.0, 5, 70.0, 100.0, TimeValue.ELIMINATE));
		itApplicationSecond = itApplicationDAL.save(new ITApplication(2, statusSecond, "application 2", 
				"1.20.1", LocalDate.of(2021, 01, 20), LocalDate.of(2025, 05, 20), 2, 3, 4, 5, 6, 
				7, 8, 9, "EUR", 1000.0, 4, 70.0, 100.0, TimeValue.INVEST));
	}
	
	@AfterEach
	public void close() {
		statusDAL.delete(statusFirst);
		statusDAL.delete(statusSecond);
		technologyDAL.delete(technologyFirst);
		technologyDAL.delete(technologySecond);
		itApplicationDAL.delete(itApplicationFirst);
		itApplicationDAL.delete(itApplicationSecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusDAL);
		assertNotNull(technologyDAL);
		assertNotNull(itApplicationDAL);
		assertNotNull(itApplicationService);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(technologyFirst);
		assertNotNull(technologySecond);
		assertNotNull(itApplicationFirst);
		assertNotNull(itApplicationSecond);
	}
	
	@Test
	public void should_postItApplication_whenSaveItApplication() throws Exception {
		Integer statusId = statusFirst.getStatusId();
		String name = "This a post request";
		String version = itApplicationFirst.getVersion();
		LocalDate purchaseDate = itApplicationFirst.getPurchaseDate();
		LocalDate endOfLife = itApplicationFirst.getEndOfLife();
		Integer currentScalability = itApplicationFirst.getCurrentScalability();
		Integer expectedScalability = itApplicationFirst.getExpectedScalability();
		Integer currentPerformance = itApplicationFirst.getCurrentPerformance();
		Integer expectedPerformance = itApplicationFirst.getExpectedPerformance();
		Integer currentSecurityLevel = itApplicationFirst.getCurrentSecurityLevel();
		Integer expectedSecurityLevel = itApplicationFirst.getExpectedSecurityLevel();
		Integer currentStability = itApplicationFirst.getCurrentStability();
		Integer expectedStability = itApplicationFirst.getExpectedScalability();
		String currencyType = itApplicationFirst.getCurrencyType();
		Double costCurrency = itApplicationFirst.getCostCurrency();
		Integer currentValue = itApplicationFirst.getCurrentValue();
		Double currentYearlyCost = itApplicationFirst.getCurrentYearlyCost();
		Double acceptedYearlyCost = itApplicationFirst.getAcceptedYearlyCost();
		TimeValue timeValue = itApplicationFirst.getTimeValue();
		
		MvcResult mvcResult = mockMvc.perform(post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("statusId", statusId.toString())
				.param("name", name)
				.param("version", version)
				.param("purchaseDate", purchaseDate.toString())
				.param("endOfLife", endOfLife.toString())
				.param("currentScalability", currentScalability.toString())
				.param("expectedScalability", expectedScalability.toString())
				.param("currentPerformance", currentPerformance.toString())
				.param("expectedPerformance", expectedPerformance.toString())
				.param("currentSecurityLevel", currentSecurityLevel.toString())
				.param("expectedSecurityLevel", expectedSecurityLevel.toString())
				.param("currentStability", currentStability.toString())
				.param("expectedStability", expectedStability.toString())
				.param("currencyType", currencyType)
				.param("costCurrency", costCurrency.toString())
				.param("currentValue", currentValue.toString())
				.param("currentYearlyCost", currentYearlyCost.toString())
				.param("acceptedYearlyCost", acceptedYearlyCost.toString())
				.param("timeValue", timeValue.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ITApplicationDto itapplicationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ITApplicationDto.class);
		
		ITApplication itApplication = itApplicationService.getItApplicationByName(name);
		
		assertNotNull(itapplicationDto);
		testItApplication(itApplication, itapplicationDto);
	}
	
	@Test
	public void should_getItApplication_whenGetItApplication() throws Exception {
		Integer itApplicationId = itApplicationFirst.getItApplicationId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + itApplicationId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ITApplicationDto itapplicationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ITApplicationDto.class);
		
		assertNotNull(itapplicationDto);
		testItApplication(itApplicationFirst, itapplicationDto);
	}
	
	@Test
	public void should_updateItApplication_whenUpdateItApplication() throws Exception {
		Integer itApplicationId = itApplicationFirst.getItApplicationId();
		Integer statusId = statusFirst.getStatusId();
		String name = "This an update request";
		String version = itApplicationFirst.getVersion();
		LocalDate purchaseDate = itApplicationFirst.getPurchaseDate();
		LocalDate endOfLife = itApplicationFirst.getEndOfLife();
		Integer currentScalability = itApplicationFirst.getCurrentScalability();
		Integer expectedScalability = itApplicationFirst.getExpectedScalability();
		Integer currentPerformance = itApplicationFirst.getCurrentPerformance();
		Integer expectedPerformance = itApplicationFirst.getExpectedPerformance();
		Integer currentSecurityLevel = itApplicationFirst.getCurrentSecurityLevel();
		Integer expectedSecurityLevel = itApplicationFirst.getExpectedSecurityLevel();
		Integer currentStability = itApplicationFirst.getCurrentStability();
		Integer expectedStability = itApplicationFirst.getExpectedScalability();
		String currencyType = itApplicationFirst.getCurrencyType();
		Double costCurrency = itApplicationFirst.getCostCurrency();
		Integer currentValue = itApplicationFirst.getCurrentValue();
		Double currentYearlyCost = itApplicationFirst.getCurrentYearlyCost();
		Double acceptedYearlyCost = itApplicationFirst.getAcceptedYearlyCost();
		TimeValue timeValue = itApplicationFirst.getTimeValue();
		
		MvcResult mvcResult = mockMvc.perform(put(PATH + itApplicationId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("statusId", statusId.toString())
				.param("name", name)
				.param("version", version)
				.param("purchaseDate", purchaseDate.toString())
				.param("endOfLife", endOfLife.toString())
				.param("currentScalability", currentScalability.toString())
				.param("expectedScalability", expectedScalability.toString())
				.param("currentPerformance", currentPerformance.toString())
				.param("expectedPerformance", expectedPerformance.toString())
				.param("currentSecurityLevel", currentSecurityLevel.toString())
				.param("expectedSecurityLevel", expectedSecurityLevel.toString())
				.param("currentStability", currentStability.toString())
				.param("expectedStability", expectedStability.toString())
				.param("currencyType", currencyType)
				.param("costCurrency", costCurrency.toString())
				.param("currentValue", currentValue.toString())
				.param("currentYearlyCost", currentYearlyCost.toString())
				.param("acceptedYearlyCost", acceptedYearlyCost.toString())
				.param("timeValue", timeValue.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ITApplicationDto itapplicationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ITApplicationDto.class);
		
		ITApplication itApplication = itApplicationService.getItApplicationByName(name);
		
		assertNotNull(itapplicationDto);
		testItApplication(itApplication, itapplicationDto);
	}
	
	@Test
	public void should_deleteItApplication_whenDeleteItApplication() throws Exception {
		Integer itApplicationId = itApplicationFirst.getItApplicationId();
		
		mockMvc.perform(delete(PATH + itApplicationId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_getTrue_whenItApplicationExistsById() throws Exception {
		Integer itApplicationId = itApplicationFirst.getItApplicationId();
		
		mockMvc.perform(get(PATH + "exists-by-id/" + itApplicationId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("true"));
	}
	
	@Test
	public void should_getTrue_whenItApplicationExistsByName() throws Exception {
		String itApplicationName = itApplicationFirst.getName();
		
		mockMvc.perform(get(PATH + "exists-by-name/" + itApplicationName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("true"));
	}
	
	@Test
	public void should_getItApplication_whenGetItApplicationByName() throws Exception {
		String itApplicationName = itApplicationFirst.getName();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "itapplicationname/" + itApplicationName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ITApplicationDto itapplicationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ITApplicationDto.class);
		
		assertNotNull(itapplicationDto);
		testItApplication(itApplicationFirst, itapplicationDto);
	}
	
	@Test
	public void should_getAllItApplications_whenGetAllItApplication() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<ITApplicationDto> itapplicationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<ITApplicationDto>>() {});
		
		assertNotNull(itapplicationDto);
		testItApplication(itApplicationFirst, itapplicationDto.get(0));
		testItApplication(itApplicationSecond, itapplicationDto.get(1));
	}
	
	@Test
	public void should_getAllCurrencies_whenGetAllCurrencies() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH + "all-currencies"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<String> currencies = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<String>>() {});
		
		List<String> testCurrencies = Currency.getAvailableCurrencies().stream()
				.map(currency -> currency.getCurrencyCode())
				.collect(Collectors.toList());
		
		assertNotNull(currencies);
		assertEquals(testCurrencies, currencies);
	}
	
	@Test
	public void should_getAllTimeValues_whenGetAllTimeValues() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH + "all-timevalues"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<String> timeValues = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<String>>() {});
		
		List<String> testTimeValues = Arrays.stream(TimeValue.values()).map(TimeValue::name).collect(Collectors.toList());
		
		assertNotNull(timeValues);
		assertEquals(testTimeValues, timeValues);
	}
	
	@Test
	public void should_linkTechnology_whenLinkTechnology() throws Exception {
		Integer itApplicationId = itApplicationFirst.getItApplicationId();
		Integer technologyId = technologyFirst.getTechnologyId();
		
		mockMvc.perform(put(
				PATH + "link-technology/" + itApplicationId + "/" + technologyId))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		ITApplication itApplication = itApplicationService.get(itApplicationId);
		Technology technology = itApplication.getTechnologies().get(0);
		TechnologyDto technologyDto = new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName());
		
		testTechnology(technologyFirst, technologyDto);
	}
	
	@Test
	public void should_unlinkTechnology_whenDeleteTechnology() throws Exception {
		Integer itApplicationId = itApplicationSecond.getItApplicationId();
		Integer technologyId = technologySecond.getTechnologyId();		
		itApplicationService.addTechnology(itApplicationId, technologyId);
		
		mockMvc.perform(delete(
				PATH + "unlink-technology/" + itApplicationId + "/" + technologyId))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		ITApplication itApplication = itApplicationService.get(itApplicationId);
		List<Technology> technologies = itApplication.getTechnologies();
				
		assertEquals(itApplicationSecond.getTechnologies(), technologies);
	}
	
	@Test
	public void should_returnTrue_whenHasTechnology() throws Exception {
		Integer itApplicationId = itApplicationSecond.getItApplicationId();
		Integer technologyId = technologySecond.getTechnologyId();		
		itApplicationService.addTechnology(itApplicationId, technologyId);
		
		mockMvc.perform(get(
				PATH + "has-technology/" + itApplicationId + "/" + technologyId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	private void testItApplication(ITApplication expectedObject, ITApplicationDto actualObject) {
		assertEquals(expectedObject.getItApplicationId(), actualObject.getItApplicationId());
		assertEquals(expectedObject.getStatus().getStatusId(), actualObject.getStatus().getStatusId());
		assertEquals(expectedObject.getStatus().getValidityPeriod(), actualObject.getStatus().getValidityPeriod());
		assertEquals(expectedObject.getName(), actualObject.getName());
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
	
	@Test
	private void testTechnology(Technology expectedObject, TechnologyDto actualObject) {
		assertEquals(expectedObject.getTechnologyId(), actualObject.getTechnologyId());
		assertEquals(expectedObject.getTechnologyName(), actualObject.getTechnologyName());
	}
}
