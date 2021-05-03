package com.bavostepbros.leap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
	
	@GetMapping("/environment/get/{id}")
    public ResponseEntity<Environment> getEnvironmentById(@PathVariable("id") Integer id) {
		Environment environment = envService.get(id);
        return  new ResponseEntity<Environment>(environment, HttpStatus.OK);
    }
	
	@GetMapping("/environment/all")
	public ResponseEntity<List<Environment>> getAllEnvironments() {
		List<Environment> environments = envService.getAll();
		return new ResponseEntity<List<Environment>>(environments, HttpStatus.OK);
	}
	
	@PutMapping("/environment/update")
	public ResponseEntity<Environment> updateEnvironment(@RequestBody Environment environment) {
		envService.update(environment);
		return new ResponseEntity<Environment>(environment, HttpStatus.OK);
	}
	
	@DeleteMapping("/environment/delete/{id}")
	public ResponseEntity<Void> deleteEnvironment(@PathVariable("id") Integer id) {
		envService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
