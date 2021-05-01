package com.bavostepbros.leap.controller;

import java.util.List;

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

import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class StatusController {
	
	@Autowired
	private StatusService statusService;
	
	@PostMapping(path = "/status/add", consumes = "application/json")
	public ResponseEntity<Void> addStatus(
			@RequestBody Status status, 
			UriComponentsBuilder builder) {
		
		System.out.println(status);
		boolean flag = statusService.save(status);
		if (flag == false) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder
				.path("/status/get/{id}")
				.buildAndExpand(status.getStatusId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}
	
	@GetMapping("/status/get/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Status ID is not valid.");
		}
		Status status = statusService.get(id);
        return  new ResponseEntity<Status>(status, HttpStatus.OK);
    }
	
	@GetMapping("/status/all")
	public ResponseEntity<List<Status>> getAllStatus() {
		List<Status> status = statusService.getAll();
		return new ResponseEntity<List<Status>>(status, HttpStatus.OK);
	}
	
	@PutMapping("/status/update")
	public ResponseEntity<Status> updateStatus(@RequestBody Status status) {
		statusService.update(status);
		return new ResponseEntity<Status>(status, HttpStatus.OK);
	}
	
	@DeleteMapping("/status/delete/{id}")
	public ResponseEntity<Void> deleteStatus(@PathVariable("id") Integer id) {
		statusService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
