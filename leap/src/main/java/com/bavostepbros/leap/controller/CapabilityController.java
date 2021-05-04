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

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;

import lombok.RequiredArgsConstructor;

// @CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class CapabilityController {
	
	@Autowired
	private CapabilityService capService;
	
	@Autowired
	private EnvironmentService envService;

	@Autowired
	private StatusService statusService;
	
	// Integer environmentId, String environmentName, Integer statusId, Integer validityPeriod,
	// Integer parentCapabilityId, String capabilityName, CapabilityLevel level, boolean paceOfChange,
	// String targetOperatingModel, Integer resourceQuality, Integer informationQuality, Integer applicationFit

	@PostMapping(path = "/capability/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> addCapability(
			@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("environmentName") String environmentName,
			@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("validityPeriod") Integer validityPeriod,
			@ModelAttribute("parentCapabilityId") Integer parentCapabilityId,
			@ModelAttribute("capabilityName") String capabilityName,
			@ModelAttribute("level") CapabilityLevel level,
			@ModelAttribute("paceOfChange") boolean paceOfChange,
			@ModelAttribute("targetOperatingModel") String targetOperatingModel,
			@ModelAttribute("resourceQuality") Integer resourceQuality,
			@ModelAttribute("informationQuality") Integer informationQuality,
			@ModelAttribute("applicationFit") Integer applicationFit,
			UriComponentsBuilder builder) {
		if (environmentId == null || environmentId.equals(0) || environmentName == null || 
				environmentName.isBlank() || environmentName.isEmpty() || statusId == null || 
				statusId.equals(0) || validityPeriod == null || validityPeriod.equals(0) || 
				parentCapabilityId == null || parentCapabilityId.equals(0) || 
				capabilityName == null || capabilityName.isBlank() || capabilityName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!capService.existsByCapabilityName(capabilityName)) {
			throw new DuplicateValueException("Capability name already exists.");
		}
		
		Capability capability = capService.save(environmentId, environmentName, statusId, 
				validityPeriod, parentCapabilityId, capabilityName, level, paceOfChange, 
				targetOperatingModel, resourceQuality, informationQuality, applicationFit);	
		Integer capabilityId = capability.getCapabilityId();
		boolean flag = (capabilityId == null) ? false : true;
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder
				.path("/capability/get/{id}")
				.buildAndExpand(capabilityId).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@GetMapping("/capability/get/{id}")
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
	public ResponseEntity<Capability> updateCapability(
			@ModelAttribute("capabilityId") Integer capabilityId,
			@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("environmentName") String environmentName,
			@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("validityPeriod") Integer validityPeriod,
			@ModelAttribute("parentCapabilityId") Integer parentCapabilityId,
			@ModelAttribute("capabilityName") String capabilityName,
			@ModelAttribute("level") CapabilityLevel level,
			@ModelAttribute("paceOfChange") boolean paceOfChange,
			@ModelAttribute("targetOperatingModel") String targetOperatingModel,
			@ModelAttribute("resourceQuality") Integer resourceQuality,
			@ModelAttribute("informationQuality") Integer informationQuality,
			@ModelAttribute("applicationFit") Integer applicationFit) {
		if (capabilityId == null || capabilityId.equals(0) || capabilityName == null ||
				capabilityName.isBlank() || capabilityName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!capService.existsById(capabilityId)) {
			throw new IndexDoesNotExistException("Can not update capability if it does not exist.");
		}
		if (!capService.existsByCapabilityName(capabilityName)) {
			throw new DuplicateValueException("Capability name already exists.");
		}
		if (!envService.existsById(environmentId)) {
			throw new IndexDoesNotExistException("Environment ID does not exists.");
		}
		if (envService.existsByEnvironmentName(environmentName)) {
			throw new DuplicateValueException("Environment name does not exists.");
		}
		if (!statusService.existsById(statusId)) {
			throw new IndexDoesNotExistException("Status ID does not exists.");
		}
		if (statusService.existsByValidityPeriod(validityPeriod)) {
			throw new DuplicateValueException("Validity period does not exists.");
		}

		Capability capability = capService.update(capabilityId, environmentId, environmentName, statusId, 
				validityPeriod, parentCapabilityId, capabilityName, level, paceOfChange, 
				targetOperatingModel, resourceQuality, informationQuality, applicationFit);
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
