package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.model.CapabilityApplication;
import com.bavostepbros.leap.domain.model.dto.CapabilityApplicationDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.ITApplicationDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.service.capabilityapplicationservice.CapabilityApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/capabilityapplication/")
public class CapabilityApplicationController {

	@Autowired
	private CapabilityApplicationService capabilityApplicationService;

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PostMapping(path = "{capabilityId}/{applicationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityApplicationDto addCapabilityApplication(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("applicationId") Integer applicationId,
			@Valid @ModelAttribute("efficiencySupport") Integer efficiencySupport,
			@ModelAttribute("functionalCoverage") Integer functionalCoverage,
			@ModelAttribute("correctnessBusinessFit") Integer correctnessBusinessFit,
			@ModelAttribute("futurePotential") Integer futurePotential,
			@ModelAttribute("completeness") Integer completeness,
			@ModelAttribute("correctnessInformationFit") Integer correctnessInformationFit,
			@ModelAttribute("availability") Integer availability) {

		CapabilityApplication capabilityApplication = capabilityApplicationService.save(capabilityId, applicationId,
				efficiencySupport, functionalCoverage, correctnessBusinessFit, futurePotential, completeness,
				correctnessInformationFit, availability);
		return convertCapabilityApplication(capabilityApplication);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "{capabilityId}/{applicationId}")
	public CapabilityApplicationDto getCapabilityApplication(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("applicationId") Integer applicationId) {
		CapabilityApplication capabilityApplication = capabilityApplicationService.get(capabilityId, applicationId);
		return convertCapabilityApplication(capabilityApplication);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "{capabilityId}/{applicationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityApplicationDto updateCapabilityApplication(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("applicationId") Integer applicationId,
			@ModelAttribute("efficiencySupport") Integer efficiencySupport,
			@ModelAttribute("functionalCoverage") Integer functionalCoverage,
			@ModelAttribute("correctnessBusinessFit") Integer correctnessBusinessFit,
			@ModelAttribute("futurePotential") Integer futurePotential,
			@ModelAttribute("completeness") Integer completeness,
			@ModelAttribute("correctnessInformationFit") Integer correctnessInformationFit,
			@ModelAttribute("availability") Integer availability) {

		CapabilityApplication capabilityApplication = capabilityApplicationService.save(capabilityId, applicationId,
				efficiencySupport, functionalCoverage, correctnessBusinessFit, futurePotential, completeness,
				correctnessInformationFit, availability);
		return convertCapabilityApplication(capabilityApplication);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "{capabilityId}/{applicationId}")
	public void deleteCapabilityApplication(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("applicationId") Integer applicationId) {
		capabilityApplicationService.delete(capabilityId, applicationId);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "all-capabilityApplications-by-capabilityid/{capabilityId}")
	public List<CapabilityApplicationDto> getCapabilityApplicationByCapabilityId(@PathVariable("capabilityId") Integer capabilityId) {
		List<CapabilityApplication> capabilityApplications = capabilityApplicationService
				.getCapabilityApplicationsByCapability(capabilityId);
		List<CapabilityApplicationDto> capabilityApplicationsDto = capabilityApplications.stream()
				.map(capabilityApplication -> convertCapabilityApplication(capabilityApplication))
				.collect(Collectors.toList());
		return capabilityApplicationsDto;
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "all-capabilityApplications-by-applicationid/{applicationId}")
	public List<CapabilityApplicationDto> getCapabilityApplicationByApplicationId(@PathVariable("applicationId") Integer applicationId) {
		List<CapabilityApplication> capabilityApplications = capabilityApplicationService
				.getCapabilityApplicationsByApplication(applicationId);
		List<CapabilityApplicationDto> capabilityApplicationsDto = capabilityApplications.stream()
				.map(capabilityApplication -> convertCapabilityApplication(capabilityApplication))
				.collect(Collectors.toList());
		return capabilityApplicationsDto;
	}

	private CapabilityApplicationDto convertCapabilityApplication(CapabilityApplication capabilityApplication) {

		CapabilityDto capabilityDto = new CapabilityDto(capabilityApplication.getCapability().getCapabilityId(),
				new EnvironmentDto(capabilityApplication.getCapability().getEnvironment().getEnvironmentId(),
						capabilityApplication.getCapability().getEnvironment().getEnvironmentName()),
				new StatusDto(capabilityApplication.getCapability().getStatus().getStatusId(),
						capabilityApplication.getCapability().getStatus().getValidityPeriod()),
				capabilityApplication.getCapability().getParentCapabilityId(),
				capabilityApplication.getCapability().getCapabilityName(),
				capabilityApplication.getCapability().getCapabilityDescription(),
				capabilityApplication.getCapability().getLevel(),
				capabilityApplication.getCapability().getPaceOfChange(),
				capabilityApplication.getCapability().getTargetOperatingModel(),
				capabilityApplication.getCapability().getResourceQuality(),
				capabilityApplication.getCapability().getInformationQuality(),
				capabilityApplication.getCapability().getApplicationFit());

		ITApplicationDto itApplicationDto = new ITApplicationDto(
				capabilityApplication.getApplication().getItApplicationId(),
				new StatusDto(capabilityApplication.getApplication().getStatus().getStatusId(),
						capabilityApplication.getApplication().getStatus().getValidityPeriod()),
				capabilityApplication.getApplication().getName(), capabilityApplication.getApplication().getVersion(),
				capabilityApplication.getApplication().getPurchaseDate(),
				capabilityApplication.getApplication().getEndOfLife(),
				capabilityApplication.getApplication().getCurrentScalability(),
				capabilityApplication.getApplication().getExpectedScalability(),
				capabilityApplication.getApplication().getCurrentPerformance(),
				capabilityApplication.getApplication().getExpectedPerformance(),
				capabilityApplication.getApplication().getCurrentSecurityLevel(),
				capabilityApplication.getApplication().getExpectedSecurityLevel(),
				capabilityApplication.getApplication().getCurrentStability(),
				capabilityApplication.getApplication().getExpectedStability(),
				capabilityApplication.getApplication().getCurrencyType(),
				capabilityApplication.getApplication().getCostCurrency(),
				capabilityApplication.getApplication().getCurrentValue(),
				capabilityApplication.getApplication().getCurrentYearlyCost(),
				capabilityApplication.getApplication().getAcceptedYearlyCost(),
				capabilityApplication.getApplication().getTimeValue());

		return new CapabilityApplicationDto(capabilityDto,
				itApplicationDto, capabilityApplication.getImportance(),
				capabilityApplication.getEfficiencySupport(), capabilityApplication.getFunctionalCoverage(),
				capabilityApplication.getCorrectnessBusinessFit(), capabilityApplication.getFuturePotential(),
				capabilityApplication.getCompleteness(), capabilityApplication.getCorrectnessInformationFit(),
				capabilityApplication.getAvailability());
	}
}
