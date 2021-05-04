package com.bavostepbros.leap.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;

import lombok.RequiredArgsConstructor;

// @CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class StatusController {
	
	@Autowired
	private StatusService statusService;
	
	@PostMapping(path = "/status/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> addStatus(
			@ModelAttribute("validityPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod, 
			UriComponentsBuilder builder) {
		if (validityPeriod == null) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!statusService.existsByValidityPeriod(validityPeriod)) {
			throw new DuplicateValueException("Validity period already exists.");
		}
		
		Status status = statusService.save(validityPeriod);
		Integer statusId = status.getStatusId();
		boolean flag = (statusId == null) ? false : true;
		if (flag == false) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder
				.path("/status/get/{id}")
				.buildAndExpand(validityPeriod).toUri());
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
	public ResponseEntity<Boolean> doesValidityPeriodExists(
			@PathVariable("validityperiod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityperiod) {
		if (validityperiod == null) {
			throw new InvalidInputException("Validity Period is not valid.");
		}
		
		boolean result = (!statusService.existsByValidityPeriod(validityperiod));
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	@PutMapping(path = "/status/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Status> updateStatus(
			@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("validityPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod) {
		if (statusId == null || statusId.equals(0) || validityPeriod == null) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!statusService.existsById(statusId)) {
			throw new IndexDoesNotExistException("Can not update status if it does not exist.");
		}
		if (!statusService.existsByValidityPeriod(validityPeriod)) {
			throw new DuplicateValueException("Validity period already exists.");
		}
		
		Status status = statusService.update(statusId, validityPeriod);
		return new ResponseEntity<Status>(status, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/status/delete/{id}")
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
