package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.model.Information;
import com.bavostepbros.leap.domain.model.dto.InformationDto;
import com.bavostepbros.leap.domain.service.informationservice.InformationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/information/")
public class InformationController {

	@Autowired
	private InformationService informationService;

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public InformationDto addInformation(@Valid @ModelAttribute("informationName") String informationName,
			@Valid @ModelAttribute("informationDescription") String informationDescription) {
		Information information = informationService.save(informationName, informationDescription);
		return convertInformation(information);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "{informationId}")
	public InformationDto getInformation(@Valid @PathVariable("informationId") Integer informationId) {
		Information information = informationService.get(informationId);
		return convertInformation(information);
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "{informationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public InformationDto addInformation(@Valid @PathVariable("informationId") Integer informationId, 
			@Valid @ModelAttribute("informationName") String informationName,
			@Valid @ModelAttribute("informationDescription") String informationDescription) {
		Information information = informationService.save(informationName, informationDescription);
		return convertInformation(information);
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "{informationId}")
	public void deleteInformation(@Valid @PathVariable("informationId") Integer informationId) {
		informationService.delete(informationId);
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "informationName/{informationName}")
	public InformationDto getInformation(@Valid @PathVariable("informationName") String informationName) {
		Information information = informationService.getInformationByName(informationName);
		return convertInformation(information);
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping
	public List<InformationDto> getAllInformation() {
		List<Information> informationList = informationService.getAll();
		List<InformationDto> informationDto = informationList.stream()
				.map(information -> convertInformation(information))
				.collect(Collectors.toList());
		return informationDto;
	}

	private InformationDto convertInformation(Information information) {
		return new InformationDto(information.getInformationId(), information.getInformationName(),
				information.getInformationDescription());
	}
}
