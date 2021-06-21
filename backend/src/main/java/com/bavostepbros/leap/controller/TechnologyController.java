package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.domain.model.dto.TechnologyDto;
import com.bavostepbros.leap.domain.service.technologyservice.TechnologyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/technology/")
public class TechnologyController {

	@Autowired
	private TechnologyService technologyService;

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public TechnologyDto addTechnology(@ModelAttribute("technologyName") String technologyName) {
		Technology technology = technologyService.save(technologyName);
		return convertTechnology(technology);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "{technologyId}")
	public TechnologyDto getTechnology(@PathVariable("technologyId") Integer technologyId) {
		Technology technology = technologyService.get(technologyId);
		return convertTechnology(technology);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "{technologyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public TechnologyDto updateTechnology(@PathVariable("technologyId") Integer technologyId,
			@ModelAttribute("technologyName") String technologyName) {
		Technology technology = technologyService.update(technologyId, technologyName);
		return convertTechnology(technology);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "{technologyId}")
	public void deleteEnvironment(@PathVariable("technologyId") Integer technologyId) {
		technologyService.delete(technologyId);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping
	public List<TechnologyDto> getAllTechnologies() {
		List<Technology> technologies = technologyService.getAll();
		List<TechnologyDto> technologiesDto = technologies.stream()
				.map(technology -> convertTechnology(technology))
				.collect(Collectors.toList());
		return technologiesDto;
	}

	private TechnologyDto convertTechnology(Technology technology) {
		return new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName());
	}

}
