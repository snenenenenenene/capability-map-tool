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

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
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
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EnvironmentDto updateEnvironment(
			@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("environmentName") String environmentName) {		
		Environment environment = envService.update(environmentId, environmentName);
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
	}
	
	@DeleteMapping(path = "{environmentId}")
	public void deleteEnvironment(@PathVariable("environmentId") Integer environmentId) {		
		envService.delete(environmentId);
	}
}
