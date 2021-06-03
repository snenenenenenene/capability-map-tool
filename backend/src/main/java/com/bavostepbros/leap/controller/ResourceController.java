package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.model.Resource;
import com.bavostepbros.leap.domain.model.dto.ResourceDto;
import com.bavostepbros.leap.domain.service.resourceservice.ResourceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resource/")
public class ResourceController {

	@Autowired
	private ResourceService resourceService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResourceDto addResource(@NotBlank @ModelAttribute("resourceName") String resourceName,
			@NotBlank @ModelAttribute("resourceDescription") String resourceDescription,
			@ModelAttribute("fullTimeEquivalentYearlyValue") Double fullTimeEquivalentYearlyValue) {
		Resource resource = resourceService.save(resourceName, resourceDescription, fullTimeEquivalentYearlyValue);
		return convertResource(resource);
	}

	@GetMapping(path = "{resourceId}")
	public ResourceDto getResource(@PathVariable("resourceId") Integer resourceId) {
		Resource resource = resourceService.get(resourceId);
		return convertResource(resource);
	}

	@PutMapping(path = "{resourceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResourceDto updateResource(@PathVariable("resourceId") Integer resourceId,
			@NotBlank @ModelAttribute("resourceName") String resourceName,
			@NotBlank @ModelAttribute("resourceDescription") String resourceDescription,
			@ModelAttribute("fullTimeEquivalentYearlyValue") Double fullTimeEquivalentYearlyValue) {
		Resource resource = resourceService.update(resourceId, resourceName, resourceDescription,
				fullTimeEquivalentYearlyValue);
		return convertResource(resource);
	}
	
	@DeleteMapping(path = "{resourceId}")
	public void deleteResource(@PathVariable("resourceId") Integer resourceId) {
		resourceService.delete(resourceId);
	}
	
	@GetMapping(path = "resourcename/{resourceName}")
	public ResourceDto getResourceByName(@NotBlank @PathVariable("resourceName") String resourceName) {
		Resource resource = resourceService.getResourceByName(resourceName);
		return convertResource(resource);
	}
	
	@GetMapping
	public List<ResourceDto> getAllResources() {
		List<Resource> resources = resourceService.getAll();
		List<ResourceDto> resourcesDto = resources.stream()
				.map(resource -> convertResource(resource))
				.collect(Collectors.toList());
		return resourcesDto;
	}

	private ResourceDto convertResource(Resource resource) {
		return new ResourceDto(resource.getResourceId(), resource.getResourceName(), resource.getResourceDescription(),
				resource.getFullTimeEquivalentYearlyValue());
	}

}
