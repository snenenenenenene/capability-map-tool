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

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
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
		if (status.getValidityPeriod() == null || status.getValidityPeriod().equals(0)) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!statusService.existsByValidityPeriod(status.getValidityPeriod())) {
			throw new DuplicateValueException("Validity period already exists.");
		}
		
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
	
	@GetMapping(path = "/status/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Status ID is not valid.");
		}
		if (!statusService.existsById(id)) {
			throw new IndexDoesNotExistException("Status ID does not exists.");
		}
		
		Status status = statusService.get(id);
        return  new ResponseEntity<Status>(status, HttpStatus.OK);
    }
	
	@GetMapping(path = "/status/all")
	public ResponseEntity<List<Status>> getAllStatus() {
		List<Status> status = statusService.getAll();
		return new ResponseEntity<List<Status>>(status, HttpStatus.OK);
	}
	
	@GetMapping(path = "/status/exists/id/{id}")
	public ResponseEntity<Boolean> doesStatusExistsById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Status ID is not valid.");
		}
		
		boolean result = statusService.existsById(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	@GetMapping(path = "/status/exists/validityperiod/{validityperiod}")
	public ResponseEntity<Boolean> doesValidityPeriodExists(@PathVariable("validityperiod") Integer validityperiod) {
		if (validityperiod == null || validityperiod.equals(0)) {
			throw new InvalidInputException("Validity Period is not valid.");
		}
		
		boolean result = (!statusService.existsByValidityPeriod(validityperiod));
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	@PutMapping(path = "/status/update", consumes = "application/json")
	public ResponseEntity<Status> updateStatus(@RequestBody Status status) {
		if (status.getStatusId() == null || status.getStatusId().equals(0) ||
				status.getValidityPeriod() == null || status.getValidityPeriod().equals(0)) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!statusService.existsById(status.getStatusId())) {
			throw new IndexDoesNotExistException("Can not update status if it does not exist.");
		}
		if (!statusService.existsByValidityPeriod(status.getValidityPeriod())) {
			throw new DuplicateValueException("Validity period already exists.");
		}
		
		statusService.update(status);
		return new ResponseEntity<Status>(status, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/status/{id}")
	public ResponseEntity<Void> deleteStatus(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Status ID is not valid.");
		}
		if (!statusService.existsById(id)) {
			throw new IndexDoesNotExistException("Status ID does not exists.");
		}
		
		statusService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
