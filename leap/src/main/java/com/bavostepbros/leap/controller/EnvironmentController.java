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
		Environment environment = envService.get(id);
        return  new ResponseEntity<Environment>(environment, HttpStatus.OK);
    }
	
	@GetMapping(path = "/environment/environmentname/{environmentname}")
    public ResponseEntity<Environment> getEnvironmentByEnvironmentName(@PathVariable("environmentname") String environmentName) {
		Environment environment = envService.getByEnvironmentName(environmentName);
        return  new ResponseEntity<Environment>(environment, HttpStatus.OK);
    }
	
	@GetMapping(path = "/environment/exists/id/{id}")
	public ResponseEntity<Boolean> doesEnvironmentExistsById(@ModelAttribute("id") Integer id) {
		boolean result = envService.existsById(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	@GetMapping(path = "/environment/exists/environmentname/{environmentname}")
	public ResponseEntity<Boolean> doesEnvironmentNameExists(@PathVariable("environmentname") String environmentName) {		
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
		Environment environment = envService.update(environmentId, environmentName);
		return new ResponseEntity<Environment>(environment, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/environment/delete/{id}")
	public ResponseEntity<Void> deleteEnvironment(@PathVariable("id") Integer id) {		
		envService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
