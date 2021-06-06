package com.bavostepbros.leap.domain.service.itapplicationservice;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.RelationshipException;
import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.domain.model.timevalue.TimeValue;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.domain.service.technologyservice.TechnologyService;
import com.bavostepbros.leap.persistence.ITApplicationDAL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	@Override
	public ITApplication save(Integer statusId, String name, String version, LocalDate purchaseDate,
			LocalDate endOfLife, Integer currentScalability, Integer expectedScalability, Integer currentPerformance,
			Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel,
			Integer currentStability, Integer expectedStability, String currencyType, Double costCurrency,
			Integer currentValue, Double currentYearlyCost, Double acceptedYearlyCost, String timeValue) {
		if (name == null || name.isBlank() || name.isEmpty()) {
			throw new InvalidInputException("IT-application name is invalid.");
		}
		if (statusId == null || statusId.equals(0)) {
			throw new ForeignKeyException("Status ID is invalid.");
		}
		if (!statusService.existsById(statusId)) {
			throw new ForeignKeyException("Status ID does not exists.");
		}

		Status status = statusService.get(statusId);
		TimeValue timeValueConverted = TimeValue.valueOf(timeValue);
		ITApplication itApplication = new ITApplication(status, name, version, purchaseDate, endOfLife,
				currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
				expectedSecurityLevel, currentStability, expectedStability, currencyType, costCurrency, currentValue,
				currentYearlyCost, acceptedYearlyCost, timeValueConverted);
		return itApplicationDAL.save(itApplication);
	}

	public ITApplication get(Integer itapplicationID) {
		if (itapplicationID == null || itapplicationID.equals(0)) {
			throw new InvalidInputException("IT-application ID is not valid.");
		}
		if (!existsById(itapplicationID)) {
			throw new IndexDoesNotExistException("IT-application ID does not exists.");
		}

		return itApplicationDAL.findById(itapplicationID).get();
	}

	@Override
	public ITApplication update(Integer id, Integer statusId, String name, String version, LocalDate purchaseDate,
			LocalDate endOfLife, Integer currentScalability, Integer expectedScalability, Integer currentPerformance,
			Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel,
			Integer currentStability, Integer expectedStability, String currencyType, Double costCurrency,
			Integer currentValue, Double currentYearlyCost, Double acceptedYearlyCost, String timeValue) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("IT-application ID is invalid.");
		}
		if (name == null || name.isBlank() || name.isEmpty()) {
			throw new InvalidInputException("IT-application name is invalid.");
		}
		if (statusId == null || statusId.equals(0)) {
			throw new ForeignKeyException("Status ID is invalid.");
		}
		ITApplication oldItApplication = itApplicationDAL.findById(id).get();
		if (name != oldItApplication.getName() && existsByName(name)) {
			throw new DuplicateValueException("IT-application name already exists.");
		}
		if (!statusService.existsById(statusId)) {
			throw new ForeignKeyException("Status ID does not exists.");
		}

		Status status = statusService.get(statusId);
		TimeValue timeValueConverted = TimeValue.valueOf(timeValue);
		ITApplication itApplication = new ITApplication(id, status, name, version, purchaseDate, endOfLife,
				currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
				expectedSecurityLevel, currentStability, expectedStability, currencyType, costCurrency, currentValue,
				currentYearlyCost, acceptedYearlyCost, timeValueConverted);
		return itApplicationDAL.save(itApplication);
	}

	@Override
	public void delete(Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("IT-application ID is invalid.");
		}
		itApplicationDAL.deleteById(id);
	}

	public boolean existsById(Integer id) {
		return itApplicationDAL.existsById(id);
	}

	public boolean existsByName(String name) {
		return !itApplicationDAL.findByName(name).isEmpty();
	}

	@Override
	public ITApplication getItApplicationByName(String name) {
		if (name == null || name.isBlank() || name.isEmpty()) {
			throw new InvalidInputException("IT-application name is not valid.");
		}

		Optional<ITApplication> itApplication = itApplicationDAL.findByName(name);
		itApplication.orElseThrow(() -> new NullPointerException("IT-application does not exist."));
		return itApplication.get();
	}

	@Override
	public List<ITApplication> getAll() {
		return itApplicationDAL.findAll();
	}

	@Override
	public List<String> getAllCurrencies() {
		List<String> currencies = Currency.getAvailableCurrencies().stream()
				.map(currency -> currency.getCurrencyCode())
				.collect(Collectors.toList());
		return currencies;
	}
	
	@Override
	public List<String> getAllTimeValues() {
		return Arrays.stream(TimeValue.values()).map(TimeValue::name).collect(Collectors.toList());
	}

	@Override
	public void addTechnology(Integer itApplicationId, Integer technologyId) {
		if (itApplicationId == null || itApplicationId.equals(0)) {
			throw new InvalidInputException("IT-application ID is invalid.");
		}
		if (technologyId == null || technologyId.equals(0)) {
			throw new InvalidInputException("IT-application ID is invalid.");
		}
		if (doesItApplicationHasTechnology(itApplicationId, technologyId)) {
			throw new RelationshipException("Relationship already exsists.");
		}
		
		ITApplication itApplication = get(itApplicationId);
		Technology technology = technologyService.get(technologyId);
		itApplication.addTechnology(technology);
		return;
	}

	@Override
	public void deleteTechnology(Integer itApplicationId, Integer technologyId) {
		if (itApplicationId == null || itApplicationId.equals(0)) {
			throw new InvalidInputException("IT-application ID is invalid.");
		}
		if (technologyId == null || technologyId.equals(0)) {
			throw new InvalidInputException("IT-application ID is invalid.");
		}
		if (!doesItApplicationHasTechnology(itApplicationId, technologyId)) {
			throw new RelationshipException("Relationship does not exsist.");
		}
		
		ITApplication itApplication = get(itApplicationId);
		Technology technology = technologyService.get(technologyId);
		itApplication.removeTechnology(technology);
	}

	@Override
	public boolean doesItApplicationHasTechnology(Integer itApplicationId, Integer technologyId) {
		if (itApplicationId == null || itApplicationId.equals(0)) {
			throw new InvalidInputException("IT-application ID is invalid.");
		}
		if (technologyId == null || technologyId.equals(0)) {
			throw new InvalidInputException("IT-application ID is invalid.");
		}
		
		ITApplication itApplication = get(itApplicationId);
		Technology technology = technologyService.get(technologyId);
		return itApplication.hasTechnology(technology);
	}

}
