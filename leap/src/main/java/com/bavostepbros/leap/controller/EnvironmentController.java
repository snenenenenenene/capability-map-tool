package com.bavostepbros.leap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;

@CrossOrigin(origins = "*")
@RestController
public class EnvironmentController {
	
	@Autowired
	private EnvironmentService envService;
	
	@PostMapping(path = "/environment/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> addEnvironment(
			@ModelAttribute Environment environment,
			UriComponentsBuilder builder) {
		if (environment.getEnvironmentName() == null || 
				environment.getEnvironmentName().isBlank() || 
				environment.getEnvironmentName().isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!envService.existsByEnvironmentName(environment.getEnvironmentName())) {
			throw new DuplicateValueException("Environment name already exists.");
		}
		
		boolean flag = envService.save(environment);
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder
				.path("/environment/get/{id}")
				.buildAndExpand(environment.getEnvironmentId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/environment/{id}")
    public ResponseEntity<Environment> getEnvironmentById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Environment ID is not valid.");
		}
		if (!envService.existsById(id)) {
			throw new IndexDoesNotExistException("Environment ID does not exists.");
		}
		
		Environment environment = envService.get(id);
        return  new ResponseEntity<Environment>(environment, HttpStatus.OK);
    }
	
	@GetMapping(path = "/environment/environmentname/{environmentname}")
    public ResponseEntity<Environment> getEnvironmentByEnvironmentName(@PathVariable("environmentname") String environmentName) {
		if (environmentName == null || environmentName.isBlank() || environmentName.isEmpty()) {
			throw new InvalidInputException("Environment name is not valid.");
		}
		if (envService.existsByEnvironmentName(environmentName)) {
			throw new IndexDoesNotExistException("Environment name does not exists.");
		}
		
		Environment environment = envService.getByEnvironmentName(environmentName);
        return  new ResponseEntity<Environment>(environment, HttpStatus.OK);
    }
	
	@GetMapping(path = "/environment/exists/id/{id}")
	public ResponseEntity<Boolean> doesEnvironmentExistsById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Environment ID is not valid.");
		}
		
		boolean result = envService.existsById(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	@GetMapping(path = "/environment/exists/environmentname/{environmentname}")
	public ResponseEntity<Boolean> doesEnvironmentNameExists(@PathVariable("environmentname") String environmentName) {
		if (environmentName.isBlank() || environmentName.isEmpty()) {
			throw new InvalidInputException("Environment Name is not valid.");
		}
		
		boolean result = (!envService.existsByEnvironmentName(environmentName));
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	@GetMapping(path = "/environment/all")
	public ResponseEntity<List<Environment>> getAllEnvironments() {
		List<Environment> environments = envService.getAll();
		return new ResponseEntity<List<Environment>>(environments, HttpStatus.OK);
	}
	
	@PutMapping(path = "/environment/update", consumes = "application/json")
	public ResponseEntity<Environment> updateEnvironment(@RequestBody Environment environment) {
		if (environment.getEnvironmentId() == null || environment.getEnvironmentId().equals(0) ||
				environment.getEnvironmentName().isBlank() || environment.getEnvironmentName().isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!envService.existsById(environment.getEnvironmentId())) {
			throw new IndexDoesNotExistException("Can not update environment if it does not exist.");
		}
		if (!envService.existsByEnvironmentName(environment.getEnvironmentName())) {
			throw new DuplicateValueException("Environment name already exists.");
		}
		
		envService.update(environment);
		return new ResponseEntity<Environment>(environment, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/environment/{id}")
	public ResponseEntity<Void> deleteEnvironment(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Environment ID is not valid.");
		}
		if (!envService.existsById(id)) {
			throw new IndexDoesNotExistException("Environment ID does not exists.");
		}
		
		envService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
