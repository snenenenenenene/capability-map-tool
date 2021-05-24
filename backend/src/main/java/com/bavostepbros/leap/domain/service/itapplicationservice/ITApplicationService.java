package com.bavostepbros.leap.domain.service.itapplicationservice;

import com.bavostepbros.leap.domain.model.ITApplication;

import java.time.LocalDate;
import java.util.List;

public interface ITApplicationService {
	ITApplication save(Integer statusID, String name, String version, LocalDate purchaseDate, LocalDate endOfLife,
			Integer currentScalability, Integer expectedScalability, Integer currentPerformance,
			Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel,
			Integer currentStability, Integer expectedStability, String currencyType, Double costCurrency,
			Double currentValue, Double currentYearlyCost, Double acceptedYearlyCost, LocalDate timeValue);

	ITApplication get(Integer itApplicationId);

	ITApplication update(Integer id, Integer statusID, String name, String version, LocalDate purchaseDate,
			LocalDate endOfLife, Integer currentScalability, Integer expectedScalability, Integer currentPerformance,
			Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel,
			Integer currentStability, Integer expectedStability, String currencyType, Double costCurrency,
			Double currentValue, Double currentYearlyCost, Double acceptedYearlyCost, LocalDate timeValue);

	void delete(Integer itApplicationID);

	boolean existsById(Integer itApplicationID);

	boolean existsByName(String name);

	ITApplication getItApplicationByName(String name);

	List<ITApplication> getAll();

	List<String> getAllCurrencies();
	
	void addTechnology(Integer itApplicationId, Integer technologyId);
}
