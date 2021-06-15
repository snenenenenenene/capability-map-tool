package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.Set;
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

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Resource;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.ResourceDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.service.resourceservice.ResourceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resource/")
public class ResourceController {

	@Autowired
	private ResourceService resourceService;

	
	/** 
	 * @param @ModelAttribute("resourceName"
	 * @return ResourceDto
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResourceDto addResource(@NotBlank @ModelAttribute("resourceName") String resourceName,
			@NotBlank @ModelAttribute("resourceDescription") String resourceDescription,
			@ModelAttribute("fullTimeEquivalentYearlyValue") Double fullTimeEquivalentYearlyValue) {
		Resource resource = resourceService.save(resourceName, resourceDescription, fullTimeEquivalentYearlyValue);
		return convertResource(resource);
	}

	
	/** 
	 * @param resourceId
	 * @return ResourceDto
	 */
	@GetMapping(path = "{resourceId}")
	public ResourceDto getResource(@PathVariable("resourceId") Integer resourceId) {
		Resource resource = resourceService.get(resourceId);
		return convertResource(resource);
	}

	
	/** 
	 * @param @PathVariable("resourceId"
	 * @return ResourceDto
	 */
	@PutMapping(path = "{resourceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResourceDto updateResource(@PathVariable("resourceId") Integer resourceId,
			@NotBlank @ModelAttribute("resourceName") String resourceName,
			@NotBlank @ModelAttribute("resourceDescription") String resourceDescription,
			@ModelAttribute("fullTimeEquivalentYearlyValue") Double fullTimeEquivalentYearlyValue) {
		Resource resource = resourceService.update(resourceId, resourceName, resourceDescription,
				fullTimeEquivalentYearlyValue);
		return convertResource(resource);
	}
	
	
	/** 
	 * @param resourceId
	 */
	@DeleteMapping(path = "{resourceId}")
	public void deleteResource(@PathVariable("resourceId") Integer resourceId) {
		resourceService.delete(resourceId);
	}
	
	
	/** 
	 * @param resourceName
	 * @return ResourceDto
	 */
	@GetMapping(path = "resourcename/{resourceName}")
	public ResourceDto getResourceByName(@NotBlank @PathVariable("resourceName") String resourceName) {
		Resource resource = resourceService.getResourceByName(resourceName);
		return convertResource(resource);
	}
	
	
	/** 
	 * @return List<ResourceDto>
	 */
	@GetMapping
	public List<ResourceDto> getAllResources() {
		List<Resource> resources = resourceService.getAll();
		List<ResourceDto> resourcesDto = resources.stream()
				.map(resource -> convertResource(resource))
				.collect(Collectors.toList());
		return resourcesDto;
	}
	
	
	/** 
	 * @param @ModelAttribute("resourceId"
	 */
	@PutMapping(path = "link-capability/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void linkCapability(@ModelAttribute("resourceId") Integer resourceId, 
			@ModelAttribute("capabilityId") Integer capabilityId) {
		resourceService.addCapability(resourceId, capabilityId);
	}
	
	
	/** 
	 * @param @ModelAttribute("resourceId"
	 */
	@DeleteMapping(path = "unlink-capability/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void unlinkCapability(@ModelAttribute("resourceId") Integer resourceId, 
			@ModelAttribute("capabilityId") Integer capabilityId) {
		resourceService.deleteCapability(resourceId, capabilityId);
	}
	
	
	/** 
	 * @param resourceId
	 * @return List<CapabilityDto>
	 */
	@GetMapping(path = "get-capabilities/{resourceId}")
	public List<CapabilityDto> getCapabilities(@PathVariable("resourceId") Integer resourceId) {
		Set<Capability> capabilities = resourceService.getAllCapabilitiesByResourceId(resourceId);
		List<CapabilityDto> capabilitiesDto = capabilities.stream()
				.map(capability -> convertCapability(capability))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	
	/** 
	 * @param resource
	 * @return ResourceDto
	 */
	private ResourceDto convertResource(Resource resource) {
		return new ResourceDto(resource.getResourceId(), resource.getResourceName(), resource.getResourceDescription(),
				resource.getFullTimeEquivalentYearlyValue());
	}
	
	
	/** 
	 * @param capability
	 * @return CapabilityDto
	 */
	private CapabilityDto convertCapability(Capability capability) {
		EnvironmentDto environmentDto = new EnvironmentDto(capability.getEnvironment().getEnvironmentId(),
				capability.getEnvironment().getEnvironmentName());
		StatusDto statusDto = new StatusDto(capability.getStatus().getStatusId(),
				capability.getStatus().getValidityPeriod());

		return new CapabilityDto(capability.getCapabilityId(), environmentDto, statusDto,
				capability.getParentCapabilityId(), capability.getCapabilityName(),
				capability.getCapabilityDescription(), capability.getLevel(), capability.getPaceOfChange(),
				capability.getTargetOperatingModel(), capability.getResourceQuality(),
				capability.getInformationQuality(), capability.getApplicationFit());
	}

}
