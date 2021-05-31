package com.bavostepbros.leap.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityItem;
import com.bavostepbros.leap.domain.model.dto.capabilitymap.CapabilityMapDto;
import com.bavostepbros.leap.domain.model.dto.capabilitymap.CapabilityMapItemDto;

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
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityItemDto;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.model.dto.StrategyDto;
import com.bavostepbros.leap.domain.model.dto.StrategyItemDto;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Bavo Van Meel
 *
 */
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
				.map(environment -> convertEnvironment(environment)).collect(Collectors.toList());
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

	@GetMapping(path = "capabilitymap/{environmentId}")
	public CapabilityMapDto getCapabilityMap(@PathVariable("environmentId") Integer environmentId) {
		try {
			return constructMap(envService.get(environmentId));
		} catch (Exception e) {
			return new CapabilityMapDto();
		}
	}

	private EnvironmentDto convertEnvironment(Environment environment) {
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
	}

	private CapabilityMapDto constructMap(Environment environment) {
		List<StrategyDto> strategiesDto = new ArrayList<StrategyDto>();
		if (environment.getStrategies() != null) {
			strategiesDto = environment.getStrategies().stream()
					.map(strategy -> convertStrategy(strategy))
					.collect(Collectors.toList());
		}

		return new CapabilityMapDto(environment.getEnvironmentId(), environment.getEnvironmentName(),
				environment.getCapabilities().stream().filter(i -> i.getParentCapabilityId().equals(0))
						.map(i -> constructGraph(i, environment.getCapabilities()))
						.collect(Collectors.toList()),
				strategiesDto);
	}

	private EnvironmentDto convertBasicEnvironment(Environment environment) {
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
	}

	private StatusDto convertBasicStatus(Status status) {
		return new StatusDto(status.getStatusId(), status.getValidityPeriod());
	}

	private StrategyDto convertStrategy(Strategy strategy) {
		List<StrategyItemDto> strategyItemsDto = new ArrayList<StrategyItemDto>();
		if (strategy.getItems() != null) {
			strategyItemsDto = strategy.getItems().stream()
					.map(strategyItem -> convertStrategyItem(strategyItem))
					.collect(Collectors.toList());
		}
		return new StrategyDto(strategy.getStrategyId(), convertBasicStatus(strategy.getStatus()),
				strategy.getStrategyName(), strategy.getTimeFrameStart(), strategy.getTimeFrameEnd(),
				convertBasicEnvironment(strategy.getEnvironment()), strategyItemsDto);
	}

	private StrategyItemDto convertStrategyItem(StrategyItem strategyItem) {
		return new StrategyItemDto(strategyItem.getItemId(), strategyItem.getStrategyItemName(),
				strategyItem.getDescription());
	}
	
	private CapabilityItemDto convertCapabilityItem(CapabilityItem capabilityItem) {
		return new CapabilityItemDto(convertStrategyItem(capabilityItem.getStrategyItem()), capabilityItem.getStrategicImportance());
	}

	private CapabilityMapItemDto constructGraph(Capability capability, List<Capability> pool) {
		List<CapabilityItemDto> capabilityItemsDto = new ArrayList<CapabilityItemDto>();
		if (capability.getCapabilityItems() != null) {
			capabilityItemsDto = capability.getCapabilityItems().stream()
					.map(capabilityItem -> convertCapabilityItem(capabilityItem))
					.collect(Collectors.toList());
		}
		return new CapabilityMapItemDto(capability.getCapabilityId(), capability.getCapabilityName(), capability.getLevel(),
				capability.isPaceOfChange(), capability.getTargetOperatingModel(), capability.getResourceQuality(),
				capability.getInformationQuality(), capability.getApplicationFit(), convertBasicStatus(capability.getStatus()),
				pool.stream()
					.filter(i -> i.getParentCapabilityId().equals(capability.getCapabilityId()))
					.map(i -> constructGraph(i, pool))
					.collect(Collectors.toList()), capabilityItemsDto);
	}
}
