package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.ProgramDto;
import com.bavostepbros.leap.domain.model.dto.ProjectDto;
import com.bavostepbros.leap.domain.model.dto.ResourceDto;

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

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Resource;
import com.bavostepbros.leap.domain.model.dto.BusinessProcessDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
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
	private CapabilityService capabilityService;

	// Add catch for status/environment not found
	// TODO Add catch for status/environment not found

	/**
	 * @param @ModelAttribute("environmentId"
	 * @return CapabilityDto
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityDto addCapability(
			@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("parentCapabilityId") Integer parentCapabilityId,
			@ModelAttribute("capabilityName") String capabilityName,
			@ModelAttribute("capabilityDescription") String capabilityDescription,
			@ModelAttribute("paceOfChange") String paceOfChange,
			@ModelAttribute("targetOperatingModel") String targetOperatingModel,
			@ModelAttribute("resourceQuality") Integer resourceQuality,
			@ModelAttribute("informationQuality") Integer informationQuality,
			@ModelAttribute("applicationFit") Integer applicationFit) {

		Capability capability = capabilityService.save(environmentId, statusId, parentCapabilityId, capabilityName,
				capabilityDescription, paceOfChange, targetOperatingModel, resourceQuality, informationQuality,
				applicationFit);
		return convertCapability(capability);
	}

	/**
	 * @param capabilityId
	 * @return CapabilityDto
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "{capabilityid}")
	public CapabilityDto getCapabilityByCapabilityid(@PathVariable("capabilityid") Integer capabilityId) {
		Capability capability = capabilityService.get(capabilityId);
		return convertCapability(capability);
	}
	
	/**
	 * @param capabilityName
	 * @return CapabilityDto
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "capabilityname/{capabilityname}")
	public CapabilityDto getCapabilityByCapabilityname(@PathVariable("capabilityname") String capabilityName) {
		Capability capability = capabilityService.getCapabilityByCapabilityName(capabilityName);
		return convertCapability(capability);
	}

	/**
	 * @param environmentId
	 * @return List<CapabilityDto>
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "all-capabilities-by-environmentid/{environmentid}")
	public List<CapabilityDto> getAllCapabilitiesByEnvironmentId(@PathVariable("environmentid") Integer environmentId) {
		List<Capability> capabilities = capabilityService.getCapabilitiesByEnvironment(environmentId);
		List<CapabilityDto> capabilitiesDto = capabilities.stream().map(capability -> convertCapability(capability))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	/**
	 * @param level
	 * @return List<CapabilityDto>
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "all-capabilities-by-level/{level}")
	public List<CapabilityDto> getAllCapabilitiesByLevel(@PathVariable("level") String level) {
		List<Capability> capabilities = capabilityService.getCapabilitiesByLevel(level);
		List<CapabilityDto> capabilitiesDto = capabilities.stream().map(capability -> convertCapability(capability))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	/**
	 * @param parentId
	 * @return List<CapabilityDto>
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "all-capabilities-by-parentcapabilityid/{parentcapabilityid}")
	public List<CapabilityDto> getAllCapabilitiesByParentCapabilityId(
			@PathVariable("parentcapabilityid") Integer parentId) {
		List<Capability> capabilities = capabilityService.getCapabilityChildren(parentId);
		List<CapabilityDto> capabilitiesDto = capabilities.stream().map(capability -> convertCapability(capability))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	/**
	 * @param parentId
	 * @return List<CapabilityDto>
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "all-capabilities-by-parentcapabilityid-and-level/{parentcapabilityid}/{level}")
	public List<CapabilityDto> getAllCapabilitiesByParentIdAndLevel(
			@PathVariable("parentcapabilityid") Integer parentId, @PathVariable("level") String level) {
		List<Capability> capabilities = capabilityService.getCapabilitiesByParentIdAndLevel(parentId, level);
		List<CapabilityDto> capabilitiesDto = capabilities.stream().map(capability -> convertCapability(capability))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}

	/**
	 * @return List<CapabilityDto>
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping
	public List<CapabilityDto> getAllCapabilities() {
		return capabilityService.getAll().stream()
				.map(capability -> convertCapability(capability))
				.collect(Collectors.toList());
	}

	/**
	 * @param id
	 * @return boolean
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "exists-by-id/{capabilityid}")
	public boolean doesCapabilityExistsById(@PathVariable("capabilityid") Integer id) {
		return capabilityService.existsById(id);
	}

	/**
	 * @param capabilityName
	 * @return boolean
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "exists-by-capabilityname/{capabilityname}")
	public boolean doesCapabilityNameExists(@PathVariable("capabilityname") String capabilityName) {
		return capabilityService.existsByCapabilityName(capabilityName);
	}

	/**
	 * @param @PathVariable("capabilityId"
	 * @return CapabilityDto
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "{capabilityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityDto updateCapability(
			@PathVariable("capabilityId") Integer capabilityId,
			@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("parentCapabilityId") Integer parentCapabilityId,
			@ModelAttribute("capabilityName") String capabilityName,
			@ModelAttribute("capabilityDescription") String capabilityDescription,
			@ModelAttribute("level") String level,
		  	@ModelAttribute("paceOfChange") String paceOfChange,
			@ModelAttribute("targetOperatingModel") String targetOperatingModel,
			@ModelAttribute("resourceQuality") Integer resourceQuality,
			@ModelAttribute("informationQuality") Integer informationQuality,
			@ModelAttribute("applicationFit") Integer applicationFit) {

		Capability capability = capabilityService.update(capabilityId, environmentId, statusId, parentCapabilityId,
				capabilityName, capabilityDescription, paceOfChange, targetOperatingModel, resourceQuality,
				informationQuality, applicationFit);
		return convertCapability(capability);
	}

	/**
	 * @param capabilityId
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "{capabilityid}")
	public void deleteCapability(@PathVariable("capabilityid") Integer capabilityId) {
		capabilityService.delete(capabilityId);
	}


	/**
	 * @param @ModelAttribute("capabilityId"
	 */
	// TODO add test
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "link-project/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void linkProject(@ModelAttribute("capabilityId") Integer capabilityId,
			@ModelAttribute("projectId") Integer projectId) {
		capabilityService.addProject(capabilityId, projectId);
	}


	/**
	 * @param @PathVariable("capabilityId"
	 */
	// TODO add test
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "unlink-project/{capabilityId}/{projectId}")
	public void unlinkProject(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("projectId") Integer projectId) {
		capabilityService.deleteProject(capabilityId, projectId);
	}

	/**
	 * @param capabilityId
	 * @return List<ProjectDto>
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "get-projects/{capabilityId}")
	public List<ProjectDto> getCapabilities(@PathVariable("capabilityId") Integer capabilityId) {
		Set<Project> projects = capabilityService.getAllProjectsByCapabilityId(capabilityId);
		List<ProjectDto> projectsDto = projects.stream().map(project -> convertProject(project))
				.collect(Collectors.toList());
		return projectsDto;
	}

	/**
	 * @param @ModelAttribute("capabilityId"
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "link-businessprocess/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void linkBusinessProcess(@ModelAttribute("capabilityId") Integer capabilityId,
			@ModelAttribute("businessProcessId") Integer businessProcessId) {
		capabilityService.addBusinessProcess(capabilityId, businessProcessId);
	}

	/**
	 * @param @PathVariable("capabilityId"
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "unlink-businessprocess/{capabilityId}/{businessProcessId}")
	public void unlinkBusinessProcess(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("businessProcessId") Integer businessProcessId) {
		capabilityService.deleteBusinessProcess(capabilityId, businessProcessId);
	}

	/**
	 * @param capabilityId
	 * @return List<BusinessProcessDto>
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "get-businessprocess/{capabilityId}")
	public List<BusinessProcessDto> getBusinessProcess(@PathVariable("capabilityId") Integer capabilityId) {
		Set<BusinessProcess> businessProcess = capabilityService.getAllBusinessProcessByCapabilityId(capabilityId);
		List<BusinessProcessDto> businessProcessDto = businessProcess.stream().map(bp -> convertBusinessProcess(bp))
				.collect(Collectors.toList());
		return businessProcessDto;
	}

	/**
	 * @param @ModelAttribute("capabilityId"
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "link-resource/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void linkResource(@ModelAttribute("capabilityId") Integer capabilityId,
			@ModelAttribute("resourceId") Integer resourceId) {
		capabilityService.addResource(capabilityId, resourceId);
	}

	/**
	 * @param @PathVariable("capabilityId"
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "unlink-resource/{capabilityId}/{resourceId}")
	public void unlinkResource(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("resourceId") Integer resourceId) {
		capabilityService.deleteResource(capabilityId, resourceId);
	}

	/**
	 * @param capabilityId
	 * @return List<ResourceDto>
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "get-resources/{capabilityId}")
	public List<ResourceDto> getResource(@PathVariable("capabilityId") Integer capabilityId) {
		Set<Resource> resources = capabilityService.getAllResourceByResourceId(capabilityId);
		List<ResourceDto> resourcesDto = resources.stream().map(resource -> convertResource(resource))
				.collect(Collectors.toList());
		return resourcesDto;
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


	/**
	 * @param project
	 * @return ProjectDto
	 */
	private ProjectDto convertProject(Project project) {
		ProgramDto program = new ProgramDto(project.getProgram().getProgramId(), project.getProgram().getProgramName());
		StatusDto status = new StatusDto(project.getStatus().getStatusId(), project.getStatus().getValidityPeriod());
		return new ProjectDto(project.getProjectId(), project.getProjectName(), program, status);
	}


	/**
	 * @param businessProcess
	 * @return BusinessProcessDto
	 */
	private BusinessProcessDto convertBusinessProcess(BusinessProcess businessProcess) {
		return new BusinessProcessDto(businessProcess.getBusinessProcessId(), businessProcess.getBusinessProcessName(),
				businessProcess.getBusinessProcessDescription());
	}


	/**
	 * @param resource
	 * @return ResourceDto
	 */
	private ResourceDto convertResource(Resource resource) {
		return new ResourceDto(resource.getResourceId(), resource.getResourceName(), resource.getResourceDescription(),
				resource.getFullTimeEquivalentYearlyValue());
	}
}
