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

import com.bavostepbros.leap.database.StatusService;
import com.bavostepbros.leap.model.Status;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequiredArgsConstructor
public class StatusController {
	
	@Autowired
	private StatusService statusService;
	
	@PostMapping(value = "/status/add")
	public ResponseEntity<Void> addStatus(
			@RequestBody Status status, 
			UriComponentsBuilder builder) {
		
		// Status status = new Status(validityPeriod);
		System.out.println(status);
		boolean flag = statusService.save(status);
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder
				.path("/status/{id}")
				.buildAndExpand(status.getStatusId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@GetMapping("/status/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable("id") Integer id) {
		Status status = statusService.get(id);
        return  new ResponseEntity<Status>(status, HttpStatus.OK);
    }
	
	@PutMapping("/status/update")
	public ResponseEntity<Status> updateStatus(@RequestBody Status status) {
		statusService.update(status);
		return new ResponseEntity<Status>(status, HttpStatus.OK);
	}
	
	@DeleteMapping("/status/{id}")
	public ResponseEntity<Void> deleteStatus(@PathVariable("id") Integer id) {
		statusService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
