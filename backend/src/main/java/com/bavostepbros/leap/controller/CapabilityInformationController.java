package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.bavostepbros.leap.domain.model.CapabilityInformation;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityInformationDto;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.InformationDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.service.capabilityinformationservice.CapabilityInformationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/capabilityinformation/")
public class CapabilityInformationController {

	@Autowired
	private CapabilityInformationService capabilityInformationService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityInformationDto addCapabilityInformation(
			@Valid @ModelAttribute("capabilityId") Integer capabilityId,
			@Valid @ModelAttribute("informationId") Integer informationId,
			@Valid @ModelAttribute("criticality") String criticality) {
		CapabilityInformation capabilityInformation = capabilityInformationService.save(capabilityId, informationId,
				criticality);
		return convertCapabilityInformation(capabilityInformation);
	}

	@GetMapping(path = "{capabilityId}/{informationId}")
	public CapabilityInformationDto getCapabilityInformation(@Valid @PathVariable("capabilityId") Integer capabilityId,
			@Valid @PathVariable("informationId") Integer informationId) {
		CapabilityInformation capabilityInformation = capabilityInformationService.get(capabilityId, informationId);
		return convertCapabilityInformation(capabilityInformation);
	}

	@PutMapping(path = "{capabilityId}/{informationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityInformationDto updateCapabilityInformation(
			@Valid @PathVariable("capabilityId") Integer capabilityId,
			@Valid @PathVariable("informationId") Integer informationId,
			@Valid @ModelAttribute("criticality") String criticality) {
		CapabilityInformation capabilityInformation = capabilityInformationService.save(capabilityId, informationId,
				criticality);
		return convertCapabilityInformation(capabilityInformation);
	}

	@DeleteMapping(path = "{capabilityId}/{informationId}")
	public void deleteCapabilityInformation(@Valid @PathVariable("capabilityId") Integer capabilityId,
			@Valid @PathVariable("informationId") Integer informationId) {
		capabilityInformationService.delete(capabilityId, informationId);
	}

	@GetMapping(path = "all-capabilityinformation-by-capabilityid/{capabilityId}")
	public List<CapabilityInformationDto> getCapabilityInformation(
			@Valid @PathVariable("capabilityId") Integer capabilityId) {
		List<CapabilityInformation> capabilityInformationList = capabilityInformationService
				.getCapabilityInformationByCapability(capabilityId);
		List<CapabilityInformationDto> capabilityInformationDto = capabilityInformationList.stream()
				.map(capabilityInformation -> convertCapabilityInformation(capabilityInformation))
				.collect(Collectors.toList());
		return capabilityInformationDto;
	}

	private CapabilityInformationDto convertCapabilityInformation(CapabilityInformation capabilityInformation) {
		CapabilityDto capabilityDto = new CapabilityDto(capabilityInformation.getCapability().getCapabilityId(),
				new EnvironmentDto(capabilityInformation.getCapability().getEnvironment().getEnvironmentId(),
						capabilityInformation.getCapability().getEnvironment().getEnvironmentName()),
				new StatusDto(capabilityInformation.getCapability().getStatus().getStatusId(),
						capabilityInformation.getCapability().getStatus().getValidityPeriod()),
				capabilityInformation.getCapability().getParentCapabilityId(),
				capabilityInformation.getCapability().getCapabilityName(),
				capabilityInformation.getCapability().getCapabilityDescription(),
				capabilityInformation.getCapability().getLevel(),
				capabilityInformation.getCapability().getPaceOfChange(),
				capabilityInformation.getCapability().getTargetOperatingModel(),
				capabilityInformation.getCapability().getResourceQuality(),
				capabilityInformation.getCapability().getInformationQuality(),
				capabilityInformation.getCapability().getApplicationFit());

		InformationDto informationDto = new InformationDto(capabilityInformation.getInformation().getInformationId(),
				capabilityInformation.getInformation().getInformationName(),
				capabilityInformation.getInformation().getInformationDescription());

		return new CapabilityInformationDto(capabilityDto, informationDto, capabilityInformation.getCriticality());
	}
}
