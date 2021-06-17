package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.dto.BusinessProcessDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.service.businessprocessservice.BusinessProcessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/businessprocess/")
public class BusinessProcessController {

	@Autowired
	private BusinessProcessService businessProcessService;
	
	// @PreAuthorize("hasRole('USER_ADMIN') or hasRole('APP_ADMIN') or hasRole('CREATING_USER')")
	// "hasAnyRole('ROLE_USER_ADMIN','APP_ADMIN', 'CREATING_USER', 'ROLE_USER')"
	//"hasRole('USER_ADMIN') or hasRole('APP_ADMIN') or hasRole('CREATING_USER')"
	// @Secured({ "USER_ADMIN", "APP_ADMIN", "CREATING_USER" })
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BusinessProcessDto addBusinessProcess(
			@Valid @ModelAttribute("businessProcessName") String businessProcessName,
			@Valid @ModelAttribute("businessProcessDescription") String businessProcessDescription) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
	    System.out.println(securityContext.getAuthentication().getName());
	    System.out.println(securityContext.getAuthentication().getAuthorities());
		BusinessProcess businessProcess = businessProcessService.save(businessProcessName, businessProcessDescription);
		return convertBusinessProcess(businessProcess);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "{businessProcessId}")
	public BusinessProcessDto getBusinessProcess(
			@PathVariable("businessProcessId") @Positive Integer businessProcessId) {
		BusinessProcess businessProcess = businessProcessService.get(businessProcessId);
		return convertBusinessProcess(businessProcess);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "{businessProcessId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BusinessProcessDto updateBusinessProcess(
			@PathVariable("businessProcessId") @Positive Integer businessProcessId,
			@ModelAttribute("businessProcessName") @NotBlank String businessProcessName,
			@ModelAttribute("businessProcessDescription") @NotBlank String businessProcessDescription) {
		BusinessProcess businessProcess = businessProcessService.update(businessProcessId, businessProcessName,
				businessProcessDescription);
		return convertBusinessProcess(businessProcess);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "{businessProcessId}")
	public void deleteBusinessProcess(@PathVariable("businessProcessId") @Positive Integer businessProcessId) {
		businessProcessService.delete(businessProcessId);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "businessProcessName/{businessProcessName}")
	public BusinessProcessDto getBusinessProcess(
			@Valid @PathVariable("businessProcessName") String businessProcessName) {
		BusinessProcess businessProcess = businessProcessService.getBusinessProcessByName(businessProcessName);
		return convertBusinessProcess(businessProcess);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping
	public List<BusinessProcessDto> getAllBusinessProcess() {
		List<BusinessProcess> businessProcessList = businessProcessService.getAll();
		List<BusinessProcessDto> businessProcessDto = businessProcessList.stream()
				.map(businessProcess -> convertBusinessProcess(businessProcess)).collect(Collectors.toList());
		return businessProcessDto;
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "link-capability/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void linkCapability(@ModelAttribute("businessProcessId") Integer businessProcessId,
			@ModelAttribute("capabilityId") Integer capabilityId) {
		businessProcessService.addCapability(businessProcessId, capabilityId);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "unlink-capability/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void unlinkCapability(@ModelAttribute("businessProcessId") Integer businessProcessId,
			@ModelAttribute("capabilityId") Integer capabilityId) {
		businessProcessService.deleteCapability(businessProcessId, capabilityId);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "get-capabilities/{businessProcessId}")
	public List<CapabilityDto> getCapabilities(@PathVariable("businessProcessId") Integer businessProcessId) {
		Set<Capability> capabilities = businessProcessService.getAllCapabilitiesByBusinessProcessId(businessProcessId);
		List<CapabilityDto> capabilitiesDto = capabilities.stream().map(capability -> convertCapability(capability))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	private BusinessProcessDto convertBusinessProcess(BusinessProcess businessProcess) {
		return new BusinessProcessDto(businessProcess.getBusinessProcessId(), businessProcess.getBusinessProcessName(),
				businessProcess.getBusinessProcessDescription());
	}

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
