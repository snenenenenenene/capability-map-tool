package com.bavostepbros.leap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bavostepbros.leap.database.EnvironmentService;
import com.bavostepbros.leap.model.Environment;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class EnvironmentController {
	
	@Autowired
	private EnvironmentService envService;
	
	@PostMapping("/environment/add")
	public ResponseEntity<Void> addEnvironment(
			@RequestBody Environment environment, 
			UriComponentsBuilder builder) {
		
		boolean flag = envService.save(environment);
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder
				.path("/environment/{id}")
				.buildAndExpand(environment.getEnvironmentId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@GetMapping("/environment/{id}")
    public ResponseEntity<Environment> getEnvironmentById(@PathVariable("id") Integer id) {
		Environment environment = envService.get(id);
        return  new ResponseEntity<Environment>(environment, HttpStatus.OK);
    }
	
	@PutMapping("/environment/update")
	public ResponseEntity<Environment> updateEnvironment(@RequestBody Environment environment) {
		envService.update(environment);
		return new ResponseEntity<Environment>(environment, HttpStatus.OK);
	}
	
	@DeleteMapping("/environment/{id}")
	public ResponseEntity<Void> deleteEnvironment(@PathVariable("id") Integer id) {
		envService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
