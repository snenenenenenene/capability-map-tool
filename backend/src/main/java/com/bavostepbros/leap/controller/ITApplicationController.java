package com.bavostepbros.leap.controller;

import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.model.dto.ITApplicationDto;
import com.bavostepbros.leap.domain.model.dto.TechnologyDto;
import com.bavostepbros.leap.domain.service.itapplicationservice.ITApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itapplication")
public class ITApplicationController {

	@Autowired
	private ITApplicationService itApplicationService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ITApplicationDto addITApplication(
			@ModelAttribute("statusId") Integer statusID,
			@ModelAttribute("name") String name, 
			@ModelAttribute("version") String version,
			@ModelAttribute("purchaseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate,
			@ModelAttribute("endOfLife") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endOfLife,
			@ModelAttribute("currentScalability") Integer currentScalability,
			@ModelAttribute("expectedScalability") Integer expectedScalability,
			@ModelAttribute("currentPerformance") Integer currentPerformance,
			@ModelAttribute("expectedPerformance") Integer expectedPerformance,
			@ModelAttribute("currentSecurityLevel") Integer currentSecurityLevel,
			@ModelAttribute("expectedSecurityLevel") Integer expectedSecurityLevel,
			@ModelAttribute("currentStability") Integer currentsStability,
			@ModelAttribute("expectedStability") Integer expectedStability,
			@ModelAttribute("currencyType") String currencyType,
			@ModelAttribute("costCurrency") Double costCurrency, 
			@ModelAttribute("currentValue") Double currentValue,
			@ModelAttribute("currentYearlyCost") Double currentYearlyCost,
			@ModelAttribute("acceptedYearlyCost") Double acceptedYearlyCost,
			@ModelAttribute("timeValue") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeValue) {

		ITApplication itApplication = itApplicationService.save(statusID, name, version, purchaseDate, endOfLife,
				currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
				expectedSecurityLevel, currentsStability, expectedStability, currencyType, costCurrency, currentValue,
				currentYearlyCost, acceptedYearlyCost, timeValue);
		List<TechnologyDto> technologies = itApplication.getTechnologies().stream()
				.map(technology -> new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName()))
				.collect(Collectors.toList());
		return new ITApplicationDto(itApplication.getItApplicationId(), itApplication.getStatus(),
				itApplication.getName(), itApplication.getVersion(), itApplication.getPurchaseDate(),
				itApplication.getEndOfLife(), itApplication.getCurrentScalability(),
				itApplication.getExpectedScalability(), itApplication.getCurrentPerformance(),
				itApplication.getExpectedPerformance(), itApplication.getCurrentSecurityLevel(),
				itApplication.getExpectedSecurityLevel(), itApplication.getCurrentStability(),
				itApplication.getExpectedStability(), itApplication.getCurrencyType(), 
				itApplication.getCostCurrency(),itApplication.getCurrentValue(), 
				itApplication.getCurrentYearlyCost(), itApplication.getAcceptedYearlyCost(), 
				itApplication.getTimeValue(), technologies);
	}

	@GetMapping(path = "{itApplicationId}")
	public ITApplicationDto getITApplicationById(@PathVariable("itApplicationId") Integer itApplicationId) {
		ITApplication itApplication = itApplicationService.get(itApplicationId);
		List<TechnologyDto> technologies = itApplication.getTechnologies().stream()
				.map(technology -> new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName()))
				.collect(Collectors.toList());
		return new ITApplicationDto(itApplication.getItApplicationId(), itApplication.getStatus(),
				itApplication.getName(), itApplication.getVersion(), itApplication.getPurchaseDate(),
				itApplication.getEndOfLife(), itApplication.getCurrentScalability(),
				itApplication.getExpectedScalability(), itApplication.getCurrentPerformance(),
				itApplication.getExpectedPerformance(), itApplication.getCurrentSecurityLevel(),
				itApplication.getExpectedSecurityLevel(), itApplication.getCurrentStability(),
				itApplication.getExpectedStability(), itApplication.getCurrencyType(), 
				itApplication.getCostCurrency(),itApplication.getCurrentValue(), 
				itApplication.getCurrentYearlyCost(), itApplication.getAcceptedYearlyCost(), 
				itApplication.getTimeValue(), technologies);
	}
	
	@PutMapping(path = "{itApplicationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ITApplicationDto updateITApplication(@PathVariable("itApplicationId") Integer itApplicationId,
			@ModelAttribute("statusId") Integer statusID, @ModelAttribute("name") String name,
			@ModelAttribute("version") String version,
			@ModelAttribute("purchaseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate,
			@ModelAttribute("endOfLife") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endOfLife,
			@ModelAttribute("currentScalability") Integer currentScalability,
			@ModelAttribute("expectedScalability") Integer expectedScalability,
			@ModelAttribute("currentPerformance") Integer currentPerformance,
			@ModelAttribute("expectedPerformance") Integer expectedPerformance,
			@ModelAttribute("currentSecurityLevel") Integer currentSecurityLevel,
			@ModelAttribute("expectedSecurityLevel") Integer expectedSecurityLevel,
			@ModelAttribute("currentStability") Integer currentsStability,
			@ModelAttribute("expectedStability") Integer expectedStability,
			@ModelAttribute("currencyType") String currencyType,
			@ModelAttribute("costCurrency") Double costCurrency, 
			@ModelAttribute("currentValue") Double currentValue,
			@ModelAttribute("currentYearlyCost") Double currentYearlyCost,
			@ModelAttribute("acceptedYearlyCost") Double acceptedYearlyCost,
			@ModelAttribute("timeValue") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeValue) {
		
		ITApplication itApplication = itApplicationService.update(itApplicationId, statusID, name, version, purchaseDate, endOfLife,
				currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
				expectedSecurityLevel, currentsStability, expectedStability, currencyType, costCurrency, currentValue,
				currentYearlyCost, acceptedYearlyCost, timeValue);
		List<TechnologyDto> technologies = itApplication.getTechnologies().stream()
				.map(technology -> new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName()))
				.collect(Collectors.toList());
		return new ITApplicationDto(itApplication.getItApplicationId(), itApplication.getStatus(),
				itApplication.getName(), itApplication.getVersion(), itApplication.getPurchaseDate(),
				itApplication.getEndOfLife(), itApplication.getCurrentScalability(),
				itApplication.getExpectedScalability(), itApplication.getCurrentPerformance(),
				itApplication.getExpectedPerformance(), itApplication.getCurrentSecurityLevel(),
				itApplication.getExpectedSecurityLevel(), itApplication.getCurrentStability(),
				itApplication.getExpectedStability(), itApplication.getCurrencyType(), 
				itApplication.getCostCurrency(),itApplication.getCurrentValue(), 
				itApplication.getCurrentYearlyCost(), itApplication.getAcceptedYearlyCost(), 
				itApplication.getTimeValue(), technologies);
	}
	
	@DeleteMapping(path = "{itApplicationId}")
	public void deleteItApplication(@PathVariable("itApplicationId") Integer itApplicationId) {
		itApplicationService.delete(itApplicationId);
	}
	
	@GetMapping(path = "exists-by-id/{itApplicationId}")
	public boolean doesItApplicationExistsById(@PathVariable("itApplicationId") Integer itApplicationId) {
		return itApplicationService.existsById(itApplicationId);
	}
	
	@GetMapping(path = "exists-by-name/{itApplicationName}")
	public boolean doesItApplicationExistsById(@PathVariable("itApplicationName") String itApplicationName) {
		return itApplicationService.existsByName(itApplicationName);
	}
	
	@GetMapping(path = "itapplicationname/{itApplicationName}")
	public ITApplicationDto getITApplicationByName(@PathVariable("itApplicationName") String itApplicationName) {
		ITApplication itApplication = itApplicationService.getItApplicationByName(itApplicationName);
		List<TechnologyDto> technologies = itApplication.getTechnologies().stream()
				.map(technology -> new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName()))
				.collect(Collectors.toList());
		return new ITApplicationDto(itApplication.getItApplicationId(), itApplication.getStatus(),
				itApplication.getName(), itApplication.getVersion(), itApplication.getPurchaseDate(),
				itApplication.getEndOfLife(), itApplication.getCurrentScalability(),
				itApplication.getExpectedScalability(), itApplication.getCurrentPerformance(),
				itApplication.getExpectedPerformance(), itApplication.getCurrentSecurityLevel(),
				itApplication.getExpectedSecurityLevel(), itApplication.getCurrentStability(),
				itApplication.getExpectedStability(), itApplication.getCurrencyType(), 
				itApplication.getCostCurrency(),itApplication.getCurrentValue(), 
				itApplication.getCurrentYearlyCost(), itApplication.getAcceptedYearlyCost(), 
				itApplication.getTimeValue(), technologies);
	}

	@GetMapping
	public List<ITApplicationDto> getAllITApplications() {
		List<ITApplication> itApplications = itApplicationService.getAll();
		List<ITApplicationDto> itApplicationsDto = itApplications.stream()
				.map(itApplication -> new ITApplicationDto(itApplication.getItApplicationId(), itApplication.getStatus(),
						itApplication.getName(), itApplication.getVersion(), itApplication.getPurchaseDate(),
						itApplication.getEndOfLife(), itApplication.getCurrentScalability(),
						itApplication.getExpectedScalability(), itApplication.getCurrentPerformance(),
						itApplication.getExpectedPerformance(), itApplication.getCurrentSecurityLevel(),
						itApplication.getExpectedSecurityLevel(), itApplication.getCurrentStability(),
						itApplication.getExpectedStability(), itApplication.getCurrencyType(), 
						itApplication.getCostCurrency(),itApplication.getCurrentValue(), 
						itApplication.getCurrentYearlyCost(), itApplication.getAcceptedYearlyCost(), 
						itApplication.getTimeValue(), itApplication.getTechnologies().stream()
						.map(technology -> new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName()))
						.collect(Collectors.toList())))
				.collect(Collectors.toList());
		return itApplicationsDto;
	}
	
	@GetMapping(path = "all-currencies")
	public List<String> getAllCurrencies() {
		return itApplicationService.getAllCurrencies();
	}
	
	@PutMapping(path = "link-technology/{itApplicationId}/{technologyId}")
	public void linkTechnology(@PathVariable("itApplicationId") Integer itApplicationId,
			@PathVariable("technologyId") Integer technologyId) {
		itApplicationService.addTechnology(itApplicationId, technologyId);
		return;
	}
}
