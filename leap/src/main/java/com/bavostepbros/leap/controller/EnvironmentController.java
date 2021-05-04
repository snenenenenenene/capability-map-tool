package com.bavostepbros.leap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;

import lombok.RequiredArgsConstructor;

// @CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class EnvironmentController {
	
	@Autowired
	private EnvironmentService envService;
	
	@PostMapping(path = "/environment/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Void> addEnvironment(
			@ModelAttribute("environmentName") String environmentName, 
			UriComponentsBuilder builder) {
		if (environmentName == null || environmentName.isBlank() || environmentName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!envService.existsByEnvironmentName(environmentName)) {
			throw new DuplicateValueException("Environment name already exists.");
		}

		Environment environment = envService.save(environmentName);
		Integer environmentId = environment.getEnvironmentId();
		boolean flag = (environmentId == null) ? false : true;
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder
				.path("/environment/get/{id}")
				.buildAndExpand(environmentId).toUri());
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
	public ResponseEntity<Boolean> doesEnvironmentExistsById(@ModelAttribute("id") Integer id) {
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
	
	@PutMapping(path = "/environment/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Environment> updateEnvironment(
			@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("environmentName") String environmentName) {
		if (environmentId == null || environmentId.equals(0) ||
				environmentName.isBlank() || environmentName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!envService.existsById(environmentId)) {
			throw new IndexDoesNotExistException("Can not update environment if it does not exist.");
		}
		if (!envService.existsByEnvironmentName(environmentName)) {
			throw new DuplicateValueException("Environment name already exists.");
		}
		
		Environment environment = envService.update(environmentId, environmentName);
		return new ResponseEntity<Environment>(environment, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/environment/delete/{id}")
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
