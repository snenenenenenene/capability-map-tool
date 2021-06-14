package com.bavostepbros.leap.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;

import lombok.RequiredArgsConstructor;

/**
*
* @author Bavo Van Meel
*
*/
// @CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/status/")
public class StatusController {
	
	@Autowired
	private StatusService statusService;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StatusDto addStatus(
			@Valid @ModelAttribute("validityPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod) {		
		Status status = statusService.save(validityPeriod);
		return new StatusDto(status.getStatusId(), status.getValidityPeriod());
	}
	
	@GetMapping(path = "{statusId}")
    public StatusDto getStatusById(@PathVariable("statusId") Integer id) {		
		Status status = statusService.get(id);
        return new StatusDto(status.getStatusId(), status.getValidityPeriod());
    }
	
	@GetMapping(path = "validityperiod/{validityPeriod}")
    public StatusDto getStatusByValidityPeriod(
    		@Valid @PathVariable("validityPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod) {		
		Status status = statusService.getByValidityPeriod(validityPeriod);
        return new StatusDto(status.getStatusId(), status.getValidityPeriod());
    }
	
	@GetMapping
	public List<StatusDto> getAllStatus() {
		List<Status> status = statusService.getAll();
		List<StatusDto> statusDto = status.stream()
				.map(s -> new StatusDto(s.getStatusId(), s.getValidityPeriod()))
				.collect(Collectors.toList());
		return statusDto;
	}
	
	@GetMapping(path = "exists-by-id/{statusId}")
	public boolean doesStatusExistsById(@PathVariable("statusId") Integer id) {		
		return statusService.existsById(id);
	}
	
	@GetMapping(path = "exists-by-validityperiod/{validityperiod}")
	public boolean doesValidityPeriodExists(
			@Valid @PathVariable("validityperiod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod) {		
		return statusService.existsByValidityPeriod(validityPeriod);
	}
	
	@PutMapping(path = "{statusId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StatusDto updateStatus(
			@PathVariable("statusId") Integer statusId,
			@Valid @ModelAttribute("validityPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod) {		
		Status status = statusService.update(statusId, validityPeriod);
		return new StatusDto(status.getStatusId(), status.getValidityPeriod());
	}
	
	@DeleteMapping(path = "{statusId}")
	public void deleteStatus(@PathVariable("statusId") Integer id) {		
		statusService.delete(id);
	}
}
