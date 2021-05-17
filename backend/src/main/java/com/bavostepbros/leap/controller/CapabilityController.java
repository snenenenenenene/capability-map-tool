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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Bavo Van Meel
 *
 */
// @CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/capability/")
public class CapabilityController {

	@Autowired
	private CapabilityService capService;

	@PostMapping(path = "add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> addCapability(
			@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("parentCapabilityId") Integer parentCapabilityId,
			@ModelAttribute("capabilityName") String capabilityName, 
			@ModelAttribute("level") String level,
			@ModelAttribute("paceOfChange") boolean paceOfChange,
			@ModelAttribute("targetOperatingModel") String targetOperatingModel,
			@ModelAttribute("resourceQuality") Integer resourceQuality,
			@ModelAttribute("informationQuality") Integer informationQuality,
			@ModelAttribute("applicationFit") Integer applicationFit, UriComponentsBuilder builder) {

		Capability capability = capService.save(environmentId, statusId, parentCapabilityId, capabilityName, level,
				paceOfChange, targetOperatingModel, resourceQuality, informationQuality, applicationFit);
		Integer capabilityId = capability.getCapabilityId();
		boolean flag = (capabilityId == null) ? false : true;
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("{id}").buildAndExpand(capabilityId).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@GetMapping("{id}")
	public ResponseEntity<Capability> getCapabilityById(@PathVariable("id") Integer id) {
		Capability capability = capService.get(id);
		return new ResponseEntity<Capability>(capability, HttpStatus.OK);
	}

	@GetMapping(path = "getallbyenvironment/{id}")
	public ResponseEntity<List<Capability>> getAllCapabilitiesByEnvironment(@PathVariable("id") Integer id) {
		List<Capability> capabilities = capService.getCapabilitiesByEnvironment(id);
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}

	@GetMapping(path = "getallbylevel/{level}")
	public ResponseEntity<List<Capability>> getAllCapabilitiesByLevel(@PathVariable("level") String level) {
		List<Capability> capabilities = capService.getCapabilitiesByLevel(level);
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}

	@GetMapping(path = "getallbyparentcapabilityid/{parentcapabilityid}")
	public ResponseEntity<List<Capability>> getAllCapabilitiesByParentCapabilityId(
			@PathVariable("parentcapabilityid") Integer parentId) {
		List<Capability> capabilities = capService.getCapabilityChildren(parentId);
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}

	@GetMapping(path = "getallbyparentidandlevel/{parentid}/{level}")
	public ResponseEntity<List<Capability>> getAllCapabilitiesByParentIdAndLevel(
			@PathVariable("parentid") Integer parentId, @PathVariable("level") String level) {
		List<Capability> capabilities = capService.getCapabilitiesByParentIdAndLevel(parentId, level);
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}

	@GetMapping(path = "all")
	public ResponseEntity<List<Capability>> getAllCapabilities() {
		List<Capability> capabilities = capService.getAll();
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}

	@GetMapping(path = "exists/id/{id}")
	public ResponseEntity<Boolean> doesCapabilityExistsById(@PathVariable("id") Integer id) {
		boolean result = capService.existsById(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@GetMapping(path = "exists/capabilityname/{capabilityname}")
	public ResponseEntity<Boolean> doesCapabilityNameExists(@PathVariable("capabilityname") String capabilityName) {
		boolean result = (!capService.existsByCapabilityName(capabilityName));
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@PutMapping(path = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Capability> updateCapability(
			@ModelAttribute("capabilityId") Integer capabilityId,
			@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("parentCapabilityId") Integer parentCapabilityId,
			@ModelAttribute("capabilityName") String capabilityName, 
			@ModelAttribute("level") String level,
			@ModelAttribute("paceOfChange") boolean paceOfChange,
			@ModelAttribute("targetOperatingModel") String targetOperatingModel,
			@ModelAttribute("resourceQuality") Integer resourceQuality,
			@ModelAttribute("informationQuality") Integer informationQuality,
			@ModelAttribute("applicationFit") Integer applicationFit) {

		Capability capability = capService.update(capabilityId, environmentId, statusId, parentCapabilityId,
				capabilityName, level, paceOfChange, targetOperatingModel, resourceQuality, informationQuality,
				applicationFit);
		return new ResponseEntity<Capability>(capability, HttpStatus.OK);
	}

	@DeleteMapping(path = "delete/{id}")
	public ResponseEntity<Void> deleteCapability(@PathVariable("id") Integer id) {
		capService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
