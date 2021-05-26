package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.bavostepbros.leap.domain.service.capabilityapplicationservice.CapabilityApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/capabilityapplication/")
public class CapabilityApplicationController {

	@Autowired
	private CapabilityApplicationService capabilityApplicationService;

	@PostMapping(path = "{capabilityId}/{applicationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityApplicationDto addCapabilityApplication(@PathVariable("capabilityId") Integer capabilityId,
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
		return new CapabilityApplicationDto(capabilityApplication.getCapability(),
				capabilityApplication.getApplication(), capabilityApplication.getImportance(),
				capabilityApplication.getEfficiencySupport(), capabilityApplication.getFunctionalCoverage(),
				capabilityApplication.getCorrectnessBusinessFit(), capabilityApplication.getFuturePotential(),
				capabilityApplication.getCompleteness(), capabilityApplication.getCorrectnessInformationFit(),
				capabilityApplication.getAvailability());
	}

	@GetMapping(path = "{capabilityId}/{applicationId}")
	public CapabilityApplicationDto getCapabilityApplication(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("applicationId") Integer applicationId) {
		CapabilityApplication capabilityApplication = capabilityApplicationService.get(capabilityId, applicationId);
		return new CapabilityApplicationDto(capabilityApplication.getCapability(),
				capabilityApplication.getApplication(), capabilityApplication.getImportance(),
				capabilityApplication.getEfficiencySupport(), capabilityApplication.getFunctionalCoverage(),
				capabilityApplication.getCorrectnessBusinessFit(), capabilityApplication.getFuturePotential(),
				capabilityApplication.getCompleteness(), capabilityApplication.getCorrectnessInformationFit(),
				capabilityApplication.getAvailability());
	}

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
		return new CapabilityApplicationDto(capabilityApplication.getCapability(),
				capabilityApplication.getApplication(), capabilityApplication.getImportance(),
				capabilityApplication.getEfficiencySupport(), capabilityApplication.getFunctionalCoverage(),
				capabilityApplication.getCorrectnessBusinessFit(), capabilityApplication.getFuturePotential(),
				capabilityApplication.getCompleteness(), capabilityApplication.getCorrectnessInformationFit(),
				capabilityApplication.getAvailability());
	}

	@DeleteMapping(path = "{capabilityId}/{applicationId}")
	public void deleteCapabilityApplication(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("applicationId") Integer applicationId) {
		capabilityApplicationService.delete(capabilityId, applicationId);
	}

	@GetMapping(path = "all-capabilityApplications-by-capabilityid/{capabilityId}")
	public List<CapabilityApplicationDto> getCapabilityApplication(@PathVariable("capabilityId") Integer capabilityId) {
		List<CapabilityApplication> capabilityApplications = capabilityApplicationService
				.getCapabilityApplicationsByCapability(capabilityId);
		List<CapabilityApplicationDto> capabilityApplicationsDto = capabilityApplications.stream()
				.map(capabilityApplication -> new CapabilityApplicationDto(capabilityApplication.getCapability(),
						capabilityApplication.getApplication(), capabilityApplication.getImportance(),
						capabilityApplication.getEfficiencySupport(), capabilityApplication.getFunctionalCoverage(),
						capabilityApplication.getCorrectnessBusinessFit(), capabilityApplication.getFuturePotential(),
						capabilityApplication.getCompleteness(), capabilityApplication.getCorrectnessInformationFit(),
						capabilityApplication.getAvailability()))
				.collect(Collectors.toList());
		return capabilityApplicationsDto;
	}
}
