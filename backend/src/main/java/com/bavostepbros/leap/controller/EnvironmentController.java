package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.bavostepbros.leap.domain.model.Capability;
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
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
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
	public EnvironmentDto addEnvironment(
			@ModelAttribute("environmentName") String environmentName) {
		Environment environment = envService.save(environmentName);
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
	}
	
	@GetMapping(path = "{environmentId}")
    public EnvironmentDto getEnvironmentById(@PathVariable("environmentId") Integer environmentId) {
		Environment environment = envService.get(environmentId);
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
    }
	
	@GetMapping(path = "environmentname/{environmentname}")
    public EnvironmentDto getEnvironmentByEnvironmentName(@PathVariable("environmentname") String environmentName) {
		Environment environment = envService.getByEnvironmentName(environmentName);
        return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
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
				.map(env -> new EnvironmentDto(env.getEnvironmentId(), env.getEnvironmentName()))
				.collect(Collectors.toList());
		return environmentsDto;
	}
	
	@PutMapping(path = "{environmentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EnvironmentDto updateEnvironment(
			@PathVariable("environmentId") Integer environmentId,
			@ModelAttribute("environmentName") String environmentName) {		
		Environment environment = envService.update(environmentId, environmentName);
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
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
			System.out.println("whoopsiedoopsie");
			return new CapabilityMapDto();
		}
	}


	private CapabilityMapDto constructMap(Environment environment) {
		return new CapabilityMapDto(environment.getEnvironmentName(),
			environment.getCapabilities().stream()
				.filter(i -> i.getParentCapabilityId().equals(0))
				.map(i -> constructGraph(i, environment.getCapabilities()))
				.collect(Collectors.toList()));
	}


	private CapabilityMapItemDto constructGraph(Capability root, List<Capability> pool) {
		return new CapabilityMapItemDto(
			root.getCapabilityName(),
			root.isPaceOfChange(),
			root.getTargetOperatingModel(),
			root.getResourceQuality(),
			root.getInformationQuality(),
			root.getApplicationFit(),
			root.getStatus(),
			pool.stream()
				.filter(i -> i.getParentCapabilityId().equals(root.getCapabilityId()))
				.map(i -> constructGraph(i, pool))
				.collect(Collectors.toList()));
	}
}
