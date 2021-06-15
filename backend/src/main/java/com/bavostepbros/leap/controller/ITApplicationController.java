package com.bavostepbros.leap.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.model.dto.ITApplicationDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.model.dto.TechnologyDto;
import com.bavostepbros.leap.domain.service.itapplicationservice.ITApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/itapplication")
public class ITApplicationController {

	@Autowired
	private ITApplicationService itApplicationService;

	
	/** 
	 * @param @ModelAttribute("statusId"
	 * @return ITApplicationDto
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ITApplicationDto addITApplication(@ModelAttribute("statusId") Integer statusID,
			@ModelAttribute("name") String name, @ModelAttribute("version") String version,
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
			@ModelAttribute("currencyType") String currencyType, @ModelAttribute("costCurrency") Double costCurrency,
			@ModelAttribute("currentValue") Integer currentValue,
			@ModelAttribute("currentYearlyCost") Double currentYearlyCost,
			@ModelAttribute("acceptedYearlyCost") Double acceptedYearlyCost,
			@ModelAttribute("timeValue") String timeValue) {

		ITApplication itApplication = itApplicationService.save(statusID, name, version, purchaseDate, endOfLife,
				currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
				expectedSecurityLevel, currentsStability, expectedStability, currencyType, costCurrency, currentValue,
				currentYearlyCost, acceptedYearlyCost, timeValue);
		return convertItApplication(itApplication);
	}

	
	/** 
	 * @param itApplicationId
	 * @return ITApplicationDto
	 */
	@GetMapping(path = "{itApplicationId}")
	public ITApplicationDto getITApplicationById(@PathVariable("itApplicationId") Integer itApplicationId) {
		ITApplication itApplication = itApplicationService.get(itApplicationId);
		return convertItApplication(itApplication);
	}

	
	/** 
	 * @param @PathVariable("itApplicationId"
	 * @return ITApplicationDto
	 */
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
			@ModelAttribute("currencyType") String currencyType, @ModelAttribute("costCurrency") Double costCurrency,
			@ModelAttribute("currentValue") Integer currentValue,
			@ModelAttribute("currentYearlyCost") Double currentYearlyCost,
			@ModelAttribute("acceptedYearlyCost") Double acceptedYearlyCost,
			@ModelAttribute("timeValue") String timeValue) {

		ITApplication itApplication = itApplicationService.update(itApplicationId, statusID, name, version,
				purchaseDate, endOfLife, currentScalability, expectedScalability, currentPerformance,
				expectedPerformance, currentSecurityLevel, expectedSecurityLevel, currentsStability, expectedStability,
				currencyType, costCurrency, currentValue, currentYearlyCost, acceptedYearlyCost, timeValue);
		return convertItApplication(itApplication);
	}

	
	/** 
	 * @param itApplicationId
	 */
	@DeleteMapping(path = "{itApplicationId}")
	public void deleteItApplication(@PathVariable("itApplicationId") Integer itApplicationId) {
		itApplicationService.delete(itApplicationId);
	}

	
	/** 
	 * @param itApplicationId
	 * @return boolean
	 */
	@GetMapping(path = "exists-by-id/{itApplicationId}")
	public boolean doesItApplicationExistsById(@PathVariable("itApplicationId") Integer itApplicationId) {
		return itApplicationService.existsById(itApplicationId);
	}

	
	/** 
	 * @param itApplicationName
	 * @return boolean
	 */
	@GetMapping(path = "exists-by-name/{itApplicationName}")
	public boolean doesItApplicationExistsById(@PathVariable("itApplicationName") String itApplicationName) {
		return itApplicationService.existsByName(itApplicationName);
	}

	
	/** 
	 * @param itApplicationName
	 * @return ITApplicationDto
	 */
	@GetMapping(path = "itapplicationname/{itApplicationName}")
	public ITApplicationDto getITApplicationByName(@PathVariable("itApplicationName") String itApplicationName) {
		ITApplication itApplication = itApplicationService.getItApplicationByName(itApplicationName);
		return convertItApplication(itApplication);
	}

	
	/** 
	 * @return List<ITApplicationDto>
	 */
	@GetMapping
	public List<ITApplicationDto> getAllITApplications() {
		List<ITApplication> itApplications = itApplicationService.getAll();
		List<ITApplicationDto> itApplicationsDto = itApplications.stream()
				.map(itApplication -> convertItApplication(itApplication)).collect(Collectors.toList());
		return itApplicationsDto;
	}

	
	/** 
	 * @return List<String>
	 */
	@GetMapping(path = "all-currencies")
	public List<String> getAllCurrencies() {
		return itApplicationService.getAllCurrencies();
	}

	
	/** 
	 * @return List<String>
	 */
	@GetMapping(path = "all-timevalues")
	public List<String> getAllTimeValues() {
		return itApplicationService.getAllTimeValues();
	}

	
	/** 
	 * @param @PathVariable("itApplicationId"
	 */
	@PutMapping(path = "link-technology/{itApplicationId}/{technologyId}")
	public void linkTechnology(@PathVariable("itApplicationId") Integer itApplicationId,
			@PathVariable("technologyId") Integer technologyId) {
		itApplicationService.addTechnology(itApplicationId, technologyId);
		return;
	}

	
	/** 
	 * @param @PathVariable("itApplicationId"
	 */
	@DeleteMapping(path = "unlink-technology/{itApplicationId}/{technologyId}")
	public void deleteTechnology(@PathVariable("itApplicationId") Integer itApplicationId,
			@PathVariable("technologyId") Integer technologyId) {
		itApplicationService.deleteTechnology(itApplicationId, technologyId);
		return;
	}

	
	/** 
	 * @param itApplication
	 * @return ITApplicationDto
	 */
	private ITApplicationDto convertItApplication(ITApplication itApplication) {
		StatusDto status = new StatusDto(itApplication.getStatus().getStatusId(),
				itApplication.getStatus().getValidityPeriod());

		List<TechnologyDto> technologies = new ArrayList<TechnologyDto>();
		if (itApplication.getTechnologies() != null) {
			technologies = itApplication.getTechnologies().stream()
					.map(technology -> new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName()))
					.collect(Collectors.toList());
		}
		
		return new ITApplicationDto(itApplication.getItApplicationId(), status, itApplication.getName(),
				itApplication.getVersion(), itApplication.getPurchaseDate(), itApplication.getEndOfLife(),
				itApplication.getCurrentScalability(), itApplication.getExpectedScalability(),
				itApplication.getCurrentPerformance(), itApplication.getExpectedPerformance(),
				itApplication.getCurrentSecurityLevel(), itApplication.getExpectedSecurityLevel(),
				itApplication.getCurrentStability(), itApplication.getExpectedStability(),
				itApplication.getCurrencyType(), itApplication.getCostCurrency(), itApplication.getCurrentValue(),
				itApplication.getCurrentYearlyCost(), itApplication.getAcceptedYearlyCost(),
				itApplication.getTimeValue(), technologies);
	}
	
}
