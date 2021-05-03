package com.bavostepbros.leap.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class CapabilityController {
	
	@Autowired
	private CapabilityService capService;
	
	@Autowired
	private EnvironmentService envService;

	@Autowired
	private StatusService statusService;

	@PostMapping(path = "/capability/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> addCapability(
			@ModelAttribute Capability capability,
			UriComponentsBuilder builder) {
		if (capability.getCapabilityName() == null ||
				capability.getCapabilityName().isBlank() ||
				capability.getCapabilityName().isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!capService.existsByCapabilityName(capability.getCapabilityName())) {
			throw new DuplicateValueException("Capability name already exists.");
		}

		boolean flag = capService.save(capability);
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder
				.path("/capability/get/{id}")
				.buildAndExpand(capability.getCapabilityId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/capability/{id}")
    public ResponseEntity<Capability> getCapabilityById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Capability ID is not valid.");
		}
		if (!capService.existsById(id)) {
			throw new IndexDoesNotExistException("Capability ID does not exists.");
		}

		Capability capability = capService.get(id);
        return  new ResponseEntity<Capability>(capability, HttpStatus.OK);
    }
	
	@GetMapping(path = "/capability/getallbyenvironment/{id}")
	public ResponseEntity<List<Capability>> getAllCapabilitiesByEnvironment(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Environment ID is not valid.");
		}
		if (!envService.existsById(id)) {
			throw new IndexDoesNotExistException("Environment ID does not exists.");
		}

		List<Capability> capabilities = capService.getCapabilitiesByEnvironment(id);
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}

	@GetMapping(path = "/capability/getallbylevel/{level}")
	public ResponseEntity<List<Capability>> getAllCapabilitiesByLevel(@PathVariable("level") CapabilityLevel level) {
		if (level == null || level.equals(0)) {
			throw new InvalidInputException("Level is not valid.");
		}
		// TODO Check if level is in bounds. (Enum class)

		List<Capability> capabilities = capService.getCapabilitiesByLevel(level);
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}

	@GetMapping(path = "/capability/getallbyparentcapabilityid/{parentcapabilityid}")
	public ResponseEntity<List<Capability>> getAllCapabilitiesByParentCapabilityId(@PathVariable("parentcapabilityid") Integer parentId) {
		if (parentId == null || parentId.equals(0)) {
			throw new InvalidInputException("Parent ID is not valid.");
		}
		if (!envService.existsById(parentId)) {
			throw new IndexDoesNotExistException("Parent ID does not exists.");
		}

		List<Capability> capabilities = capService.getCapabilityChildren(parentId);
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}

	@GetMapping(path = "/capability/getallbyparentidandlevel/{parentid}/{level}")
	public ResponseEntity<List<Capability>> getAllCapabilitiesByParentIdAndLevel(
			@PathVariable("parentid") Integer parentId,
			@PathVariable("level") CapabilityLevel level) {
		if (parentId == null || parentId.equals(0)) {
			throw new InvalidInputException("Parent ID is not valid.");
		}
		if (!envService.existsById(parentId)) {
			throw new IndexDoesNotExistException("Parent ID does not exists.");
		}
		if (level == null || level.equals(0)) {
			throw new InvalidInputException("Level is not valid.");
		}
		// TODO Check if level is in bounds. (Enum class)

		List<Capability> capabilities = capService.getCapabilitiesByParentIdAndLevel(parentId, level);
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}

	@GetMapping(path = "/capability/all")
	public ResponseEntity<List<Capability>> getAllCapabilities() {
		List<Capability> capabilities = capService.getAll();
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}

	@GetMapping(path = "/capability/exists/id/{id}")
	public ResponseEntity<Boolean> doesCapabilityExistsById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Capability ID is not valid.");
		}

		boolean result = capService.existsById(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@GetMapping(path = "/capability/exists/capabilityname/{capabilityname}")
	public ResponseEntity<Boolean> doesCapabilityNameExists(@PathVariable("capabilityname") String capabilityName) {
		if (capabilityName == null ||
				capabilityName.isBlank() ||
				capabilityName.isEmpty()) {
			throw new InvalidInputException("Input is not valid.");
		}

		boolean result = (!capService.existsByCapabilityName(capabilityName));
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
	@PutMapping(path = "/capability/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Capability> updateCapability(@ModelAttribute Capability capability) {
		if (capability.getCapabilityId() == null ||
				capability.getCapabilityId().equals(0) ||
				capability.getCapabilityName() == null ||
				capability.getCapabilityName().isBlank() ||
				capability.getCapabilityName().isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!capService.existsById(capability.getCapabilityId())) {
			throw new IndexDoesNotExistException("Can not update capability if it does not exist.");
		}
		if (!capService.existsByCapabilityName(capability.getCapabilityName())) {
			throw new DuplicateValueException("Capability name already exists.");
		}
		if (!envService.existsById(capability.getEnvironment().getEnvironmentId())) {
			throw new IndexDoesNotExistException("Environment ID does not exists.");
		}
		if (envService.existsByEnvironmentName(capability.getEnvironment().getEnvironmentName())) {
			throw new DuplicateValueException("Environment name does not exists.");
		}
		if (!statusService.existsById(capability.getStatus().getStatusId())) {
			throw new IndexDoesNotExistException("Status ID does not exists.");
		}
		if (statusService.existsByValidityPeriod(capability.getStatus().getValidityPeriod())) {
			throw new DuplicateValueException("Validity period does not exists.");
		}

		capService.update(capability);
		return new ResponseEntity<Capability>(capability, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/capability/delete/{id}")
	public ResponseEntity<Void> deleteCapability(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Capability ID is not valid.");
		}
		if (!capService.existsById(id)) {
			throw new IndexDoesNotExistException("Capability ID does not exists.");
		}

		capService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
