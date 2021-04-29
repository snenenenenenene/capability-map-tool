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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class CapabilityController {
	
	@Autowired
	private CapabilityService capService;
	
	@PostMapping("/capability/add")
	Capability addCapability(
			@RequestBody Capability capability) {
		return capService.save(capability);
	}
	
	@GetMapping("/capability/{id}")
    public ResponseEntity<Capability> getCapabilityById(@PathVariable("id") Integer id) {
		Capability capability = capService.get(id);
        return  new ResponseEntity<Capability>(capability, HttpStatus.OK);
    }
	
	@GetMapping("/capability/all")
	public List<Capability> getAllCapabilities() {
		List<Capability> capabilities = capService.getAll();
		return capabilities;
	}
	
	@PutMapping("/capability/update")
	public ResponseEntity<Capability> updateCapability(@RequestBody Capability capability) {
		capService.update(capability);
		return new ResponseEntity<Capability>(capability, HttpStatus.OK);
	}
	
	@DeleteMapping("/capability/{id}")
	public ResponseEntity<Void> deleteCapability(@PathVariable("id") Integer id) {
		capService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
