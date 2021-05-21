package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
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

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityDto addCapability(@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("parentCapabilityId") Integer parentCapabilityId,
			@ModelAttribute("capabilityName") String capabilityName, @ModelAttribute("level") String level,
			@ModelAttribute("paceOfChange") boolean paceOfChange,
			@ModelAttribute("targetOperatingModel") String targetOperatingModel,
			@ModelAttribute("resourceQuality") Integer resourceQuality,
			@ModelAttribute("informationQuality") Integer informationQuality,
			@ModelAttribute("applicationFit") Integer applicationFit, UriComponentsBuilder builder) {

		Capability capability = capService.save(environmentId, statusId, parentCapabilityId, capabilityName, level,
				paceOfChange, targetOperatingModel, resourceQuality, informationQuality, applicationFit);
		return new CapabilityDto(capability.getCapabilityId(), capability.getEnvironment(), capability.getStatus(),
				capability.getParentCapabilityId(), capability.getCapabilityName(), capability.getLevel(),
				capability.isPaceOfChange(), capability.getTargetOperatingModel(), capability.getResourceQuality(),
				capability.getInformationQuality(), capability.getApplicationFit());
	}

	@GetMapping("{capabilityid}")
	public CapabilityDto getCapabilityByCapabilityid(@PathVariable("capabilityid") Integer capabilityId) {
		Capability capability = capService.get(capabilityId);
		return new CapabilityDto(capability.getCapabilityId(), capability.getEnvironment(), capability.getStatus(),
				capability.getParentCapabilityId(), capability.getCapabilityName(), capability.getLevel(),
				capability.isPaceOfChange(), capability.getTargetOperatingModel(), capability.getResourceQuality(),
				capability.getInformationQuality(), capability.getApplicationFit());
	}

	@GetMapping("capabilityname/{capabilityname}")
	public CapabilityDto getCapabilityByCapabilityname(@PathVariable("capabilityname") String capabilityName) {
		Capability capability = capService.getCapabilityByCapabilityName(capabilityName);
		return new CapabilityDto(capability.getCapabilityId(), capability.getEnvironment(), capability.getStatus(),
				capability.getParentCapabilityId(), capability.getCapabilityName(), capability.getLevel(),
				capability.isPaceOfChange(), capability.getTargetOperatingModel(), capability.getResourceQuality(),
				capability.getInformationQuality(), capability.getApplicationFit());
	}

	@GetMapping(path = "all-capabilities-by-environmentid/{environmentid}")
	public List<CapabilityDto> getAllCapabilitiesByEnvironmentId(@PathVariable("environmentid") Integer environmentId) {
		List<Capability> capabilities = capService.getCapabilitiesByEnvironment(environmentId);
		List<CapabilityDto> capabilitiesDto = capabilities.stream()
				.map(capability -> new CapabilityDto(capability.getCapabilityId(), capability.getEnvironment(),
						capability.getStatus(), capability.getParentCapabilityId(), capability.getCapabilityName(),
						capability.getLevel(), capability.isPaceOfChange(), capability.getTargetOperatingModel(),
						capability.getResourceQuality(), capability.getInformationQuality(),
						capability.getApplicationFit()))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	@GetMapping(path = "all-capabilities-by-level/{level}")
	public List<CapabilityDto> getAllCapabilitiesByLevel(@PathVariable("level") String level) {
		List<Capability> capabilities = capService.getCapabilitiesByLevel(level);
		List<CapabilityDto> capabilitiesDto = capabilities.stream()
				.map(capability -> new CapabilityDto(capability.getCapabilityId(), capability.getEnvironment(),
						capability.getStatus(), capability.getParentCapabilityId(), capability.getCapabilityName(),
						capability.getLevel(), capability.isPaceOfChange(), capability.getTargetOperatingModel(),
						capability.getResourceQuality(), capability.getInformationQuality(),
						capability.getApplicationFit()))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	@GetMapping(path = "all-capabilities-by-parentcapabilityid/{parentcapabilityid}")
	public List<CapabilityDto> getAllCapabilitiesByParentCapabilityId(
			@PathVariable("parentcapabilityid") Integer parentId) {
		List<Capability> capabilities = capService.getCapabilityChildren(parentId);
		List<CapabilityDto> capabilitiesDto = capabilities.stream()
				.map(capability -> new CapabilityDto(capability.getCapabilityId(), capability.getEnvironment(),
						capability.getStatus(), capability.getParentCapabilityId(), capability.getCapabilityName(),
						capability.getLevel(), capability.isPaceOfChange(), capability.getTargetOperatingModel(),
						capability.getResourceQuality(), capability.getInformationQuality(),
						capability.getApplicationFit()))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	@GetMapping(path = "all-capabilities-by-parentcapabilityid-and-level/{parentcapabilityid}/{level}")
	public List<CapabilityDto> getAllCapabilitiesByParentIdAndLevel(
			@PathVariable("parentcapabilityid") Integer parentId, @PathVariable("level") String level) {
		List<Capability> capabilities = capService.getCapabilitiesByParentIdAndLevel(parentId, level);
		List<CapabilityDto> capabilitiesDto = capabilities.stream()
				.map(capability -> new CapabilityDto(capability.getCapabilityId(), capability.getEnvironment(),
						capability.getStatus(), capability.getParentCapabilityId(), capability.getCapabilityName(),
						capability.getLevel(), capability.isPaceOfChange(), capability.getTargetOperatingModel(),
						capability.getResourceQuality(), capability.getInformationQuality(),
						capability.getApplicationFit()))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	@GetMapping
	public List<CapabilityDto> getAllCapabilities() {
		List<Capability> capabilities = capService.getAll();
		List<CapabilityDto> capabilitiesDto = capabilities.stream()
				.map(capability -> new CapabilityDto(capability.getCapabilityId(), capability.getEnvironment(),
						capability.getStatus(), capability.getParentCapabilityId(), capability.getCapabilityName(),
						capability.getLevel(), capability.isPaceOfChange(), capability.getTargetOperatingModel(),
						capability.getResourceQuality(), capability.getInformationQuality(),
						capability.getApplicationFit()))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	@GetMapping(path = "exists-by-id/{capabilityid}")
	public boolean doesCapabilityExistsById(@PathVariable("capabilityid") Integer id) {
		return capService.existsById(id);
	}

	@GetMapping(path = "exists-by-capabilityname/{capabilityname}")
	public boolean doesCapabilityNameExists(@PathVariable("capabilityname") String capabilityName) {
		return capService.existsByCapabilityName(capabilityName);
	}

	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityDto updateCapability(@ModelAttribute("capabilityId") Integer capabilityId,
			@ModelAttribute("environmentId") Integer environmentId, @ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("parentCapabilityId") Integer parentCapabilityId,
			@ModelAttribute("capabilityName") String capabilityName, @ModelAttribute("level") String level,
			@ModelAttribute("paceOfChange") boolean paceOfChange,
			@ModelAttribute("targetOperatingModel") String targetOperatingModel,
			@ModelAttribute("resourceQuality") Integer resourceQuality,
			@ModelAttribute("informationQuality") Integer informationQuality,
			@ModelAttribute("applicationFit") Integer applicationFit) {

		Capability capability = capService.update(capabilityId, environmentId, statusId, parentCapabilityId,
				capabilityName, level, paceOfChange, targetOperatingModel, resourceQuality, informationQuality,
				applicationFit);
		return new CapabilityDto(capability.getCapabilityId(), capability.getEnvironment(), capability.getStatus(),
				capability.getParentCapabilityId(), capability.getCapabilityName(), capability.getLevel(),
				capability.isPaceOfChange(), capability.getTargetOperatingModel(), capability.getResourceQuality(),
				capability.getInformationQuality(), capability.getApplicationFit());
	}

	@DeleteMapping(path = "{capabilityid}")
	public void deleteCapability(@PathVariable("capabilityid") Integer capabilityId) {
		capService.delete(capabilityId);
	}
}
