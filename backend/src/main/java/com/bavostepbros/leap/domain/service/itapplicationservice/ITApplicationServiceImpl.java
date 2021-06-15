package com.bavostepbros.leap.domain.service.itapplicationservice;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.domain.model.timevalue.TimeValue;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.domain.service.technologyservice.TechnologyService;
import com.bavostepbros.leap.persistence.ITApplicationDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ITApplicationServiceImpl implements ITApplicationService {

	@Autowired
	private ITApplicationDAL itApplicationDAL;

	@Autowired
	private StatusService statusService;
	
	@Autowired
	private TechnologyService technologyService;

	
	/** 
	 * @param statusId
	 * @param name
	 * @param version
	 * @param purchaseDate
	 * @param endOfLife
	 * @param currentScalability
	 * @param expectedScalability
	 * @param currentPerformance
	 * @param expectedPerformance
	 * @param currentSecurityLevel
	 * @param expectedSecurityLevel
	 * @param currentStability
	 * @param expectedStability
	 * @param currencyType
	 * @param costCurrency
	 * @param currentValue
	 * @param currentYearlyCost
	 * @param acceptedYearlyCost
	 * @param timeValue
	 * @return ITApplication
	 */
	@Override
	public ITApplication save(Integer statusId, String name, String version, LocalDate purchaseDate,
			LocalDate endOfLife, Integer currentScalability, Integer expectedScalability, Integer currentPerformance,
			Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel,
			Integer currentStability, Integer expectedStability, String currencyType, Double costCurrency,
			Integer currentValue, Double currentYearlyCost, Double acceptedYearlyCost, String timeValue) {

		Status status = statusService.get(statusId);
		TimeValue timeValueConverted = TimeValue.valueOf(timeValue);
		ITApplication itApplication = new ITApplication(status, name, version, purchaseDate, endOfLife,
				currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
				expectedSecurityLevel, currentStability, expectedStability, currencyType, costCurrency, currentValue,
				currentYearlyCost, acceptedYearlyCost, timeValueConverted);
		return itApplicationDAL.save(itApplication);
	}

	
	/** 
	 * @param itapplicationID
	 * @return ITApplication
	 */
	public ITApplication get(Integer itapplicationID) {
		return itApplicationDAL.findById(itapplicationID).get();
	}

	
	/** 
	 * @param id
	 * @param statusId
	 * @param name
	 * @param version
	 * @param purchaseDate
	 * @param endOfLife
	 * @param currentScalability
	 * @param expectedScalability
	 * @param currentPerformance
	 * @param expectedPerformance
	 * @param currentSecurityLevel
	 * @param expectedSecurityLevel
	 * @param currentStability
	 * @param expectedStability
	 * @param currencyType
	 * @param costCurrency
	 * @param currentValue
	 * @param currentYearlyCost
	 * @param acceptedYearlyCost
	 * @param timeValue
	 * @return ITApplication
	 */
	@Override
	public ITApplication update(Integer id, Integer statusId, String name, String version, LocalDate purchaseDate,
			LocalDate endOfLife, Integer currentScalability, Integer expectedScalability, Integer currentPerformance,
			Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel,
			Integer currentStability, Integer expectedStability, String currencyType, Double costCurrency,
			Integer currentValue, Double currentYearlyCost, Double acceptedYearlyCost, String timeValue) {

		Status status = statusService.get(statusId);
		TimeValue timeValueConverted = TimeValue.valueOf(timeValue);
		ITApplication itApplication = new ITApplication(id, status, name, version, purchaseDate, endOfLife,
				currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
				expectedSecurityLevel, currentStability, expectedStability, currencyType, costCurrency, currentValue,
				currentYearlyCost, acceptedYearlyCost, timeValueConverted);
		return itApplicationDAL.save(itApplication);
	}

	
	/** 
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		itApplicationDAL.deleteById(id);
	}

	
	/** 
	 * @param id
	 * @return boolean
	 */
	public boolean existsById(Integer id) {
		return itApplicationDAL.existsById(id);
	}

	
	/** 
	 * @param name
	 * @return boolean
	 */
	public boolean existsByName(String name) {
		return !itApplicationDAL.findByName(name).isEmpty();
	}

	
	/** 
	 * @param name
	 * @return ITApplication
	 */
	@Override
	public ITApplication getItApplicationByName(String name) {
		Optional<ITApplication> itApplication = itApplicationDAL.findByName(name);
		itApplication.orElseThrow(() -> new NullPointerException("IT-application does not exist."));
		return itApplication.get();
	}

	
	/** 
	 * @return List<ITApplication>
	 */
	@Override
	public List<ITApplication> getAll() {
		return itApplicationDAL.findAll();
	}

	
	/** 
	 * @return List<String>
	 */
	@Override
	public List<String> getAllCurrencies() {
		List<String> currencies = Currency.getAvailableCurrencies().stream()
				.map(currency -> currency.getCurrencyCode())
				.collect(Collectors.toList());
		return currencies;
	}
	
	
	/** 
	 * @return List<String>
	 */
	@Override
	public List<String> getAllTimeValues() {
		return Arrays.stream(TimeValue.values()).map(TimeValue::name).collect(Collectors.toList());
	}

	
	/** 
	 * @param itApplicationId
	 * @param technologyId
	 */
	@Override
	public void addTechnology(Integer itApplicationId, Integer technologyId) {
		ITApplication itApplication = get(itApplicationId);
		Technology technology = technologyService.get(technologyId);
		itApplication.addTechnology(technology);
		return;
	}

	
	/** 
	 * @param itApplicationId
	 * @param technologyId
	 */
	@Override
	public void deleteTechnology(Integer itApplicationId, Integer technologyId) {
		ITApplication itApplication = get(itApplicationId);
		Technology technology = technologyService.get(technologyId);
		itApplication.removeTechnology(technology);
	}

}
