package com.bavostepbros.leap.controller;

import java.util.ArrayList;
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

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Bavo Van Meel
 *
 */
// @CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/environment/")
public class EnvironmentController {

	@Autowired
	private EnvironmentService envService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EnvironmentDto addEnvironment(@ModelAttribute("environmentName") String environmentName) {
		Environment environment = envService.save(environmentName);
		return convertEnvironment(environment);
	}

	@GetMapping(path = "{environmentId}")
	public EnvironmentDto getEnvironmentById(@PathVariable("environmentId") Integer environmentId) {
		Environment environment = envService.get(environmentId);
		return convertEnvironment(environment);
	}

	@GetMapping(path = "environmentname/{environmentname}")
	public EnvironmentDto getEnvironmentByEnvironmentName(@PathVariable("environmentname") String environmentName) {
		Environment environment = envService.getByEnvironmentName(environmentName);
		return convertEnvironment(environment);
	}

	@GetMapping(path = "exists-by-id/{environmentId}")
	public boolean doesEnvironmentExistsById(@ModelAttribute("environmentId") Integer environmentId) {
		return envService.existsById(environmentId);
	}

	@GetMapping(path = "exists-by-environmentname/{environmentname}")
	public boolean doesEnvironmentNameExists(@PathVariable("environmentname") String environmentName) {
		return envService.existsByEnvironmentName(environmentName);
	}

	@GetMapping
	public List<EnvironmentDto> getAllEnvironments() {
		List<Environment> environments = envService.getAll();
		List<EnvironmentDto> environmentsDto = environments.stream()
				.map(environment -> convertEnvironment(environment))
				.collect(Collectors.toList());
		return environmentsDto;
	}

	@PutMapping(path = "{environmentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EnvironmentDto updateEnvironment(@PathVariable("environmentId") Integer environmentId,
			@ModelAttribute("environmentName") String environmentName) {
		Environment environment = envService.update(environmentId, environmentName);
		return convertEnvironment(environment);
	}

	@DeleteMapping(path = "{environmentId}")
	public void deleteEnvironment(@PathVariable("environmentId") Integer environmentId) {
		envService.delete(environmentId);
	}

	private EnvironmentDto convertEnvironment(Environment environment) {
		List<CapabilityDto> capabilitiesDto = new ArrayList<CapabilityDto>();
		if (environment.getCapabilities() != null) {
			capabilitiesDto = environment.getCapabilities().stream().map(capability -> new CapabilityDto(
					capability.getCapabilityId(),
					new EnvironmentDto(capability.getEnvironment().getEnvironmentId(),
							capability.getEnvironment().getEnvironmentName()),
					new StatusDto(capability.getStatus().getStatusId(), capability.getStatus().getValidityPeriod()),
					capability.getParentCapabilityId(), capability.getCapabilityName(), capability.getLevel(),
					capability.isPaceOfChange(), capability.getTargetOperatingModel(), capability.getResourceQuality(),
					capability.getInformationQuality(), capability.getApplicationFit())).collect(Collectors.toList());
		}
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName(), capabilitiesDto);
	}
}
