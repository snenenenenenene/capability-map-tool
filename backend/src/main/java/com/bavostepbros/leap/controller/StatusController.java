package com.bavostepbros.leap.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/status/")
public class StatusController {
	
	@Autowired
	private StatusService statusService;
	
	@PostMapping(path = "add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StatusDto addStatus(
			@ModelAttribute("validityPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod) {		
		Status status = statusService.save(validityPeriod);
		return new StatusDto(status.getStatusId(), status.getValidityPeriod());
	}
	
	@GetMapping(path = "{id}")
    public StatusDto getStatusById(@PathVariable("id") Integer id) {		
		Status status = statusService.get(id);
        return new StatusDto(status.getStatusId(), status.getValidityPeriod());
    }
	
	@GetMapping(path = "validityperiod/{validityPeriod}")
    public StatusDto getStatusByValidityPeriod(
    		@PathVariable("validityPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod) {		
		Status status = statusService.getByValidityPeriod(validityPeriod);
        return new StatusDto(status.getStatusId(), status.getValidityPeriod());
    }
	
	@GetMapping(path = "all")
	public List<StatusDto> getAllStatus() {
		List<Status> status = statusService.getAll();
		List<StatusDto> statusDto = status.stream()
				.map(s -> new StatusDto(s.getStatusId(), s.getValidityPeriod()))
				.collect(Collectors.toList());
		return statusDto;
	}
	
	@GetMapping(path = "exists/id/{id}")
	public boolean doesStatusExistsById(@PathVariable("id") Integer id) {		
		return statusService.existsById(id);
	}
	
	@GetMapping(path = "exists/validityperiod/{validityperiod}")
	public boolean doesValidityPeriodExists(
			@PathVariable("validityperiod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod) {		
		return statusService.existsByValidityPeriod(validityPeriod);
	}
	
	@PutMapping(path = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StatusDto updateStatus(
			@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("validityPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validityPeriod) {		
		Status status = statusService.update(statusId, validityPeriod);
		return new StatusDto(status.getStatusId(), status.getValidityPeriod());
	}
	
	@DeleteMapping(path = "delete/{id}")
	public void deleteStatus(@PathVariable("id") Integer id) {		
		statusService.delete(id);
	}
}
