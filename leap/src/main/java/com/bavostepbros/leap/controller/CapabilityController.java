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

import com.bavostepbros.leap.database.CapabilityService;
import com.bavostepbros.leap.model.Capability;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequiredArgsConstructor
public class CapabilityController {
	
	@Autowired
	private CapabilityService capService;
	
	@PostMapping("/capability/add")
	public ResponseEntity<Void> addCapability(
			@RequestBody Capability capability,
			UriComponentsBuilder builder) {
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
	
	@GetMapping("/capability/get/{id}")
    public ResponseEntity<Capability> getCapabilityById(@PathVariable("id") Integer id) {
		Capability capability = capService.get(id);
        return  new ResponseEntity<Capability>(capability, HttpStatus.OK);
    }
	
	@GetMapping("/capability/getallbyenvironment/{id}")
	public ResponseEntity<List<Capability>> getAllCapabilitiesByEnvironment(@PathVariable("id") Integer id) {
		List<Capability> capabilities = capService.getCapabilitiesByEnvironment(id);
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}
	
	@GetMapping("/capability/all")
	public ResponseEntity<List<Capability>> getAllCapabilities() {
		List<Capability> capabilities = capService.getAll();
		return new ResponseEntity<List<Capability>>(capabilities, HttpStatus.OK);
	}
	
	@PutMapping("/capability/update")
	public ResponseEntity<Capability> updateCapability(@RequestBody Capability capability) {
		capService.update(capability);
		return new ResponseEntity<Capability>(capability, HttpStatus.OK);
	}
	
	@DeleteMapping("/capability/delete/{id}")
	public ResponseEntity<Void> deleteCapability(@PathVariable("id") Integer id) {
		capService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
