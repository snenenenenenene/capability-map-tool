package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.Set;
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

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.ProgramDto;
import com.bavostepbros.leap.domain.model.dto.ProjectDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.service.projectservice.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/project/")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProjectDto addProject(@Valid @ModelAttribute("projectName") String projectName,
			@Valid @ModelAttribute("programId") Integer programId,
			@Valid @ModelAttribute("statusId") Integer statusId) {
		Project project = projectService.save(projectName, programId, statusId);
		return convertProject(project);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "{projectId}")
	public ProjectDto getProject(@PathVariable("projectId") Integer projectId) {
		Project project = projectService.get(projectId);
		return convertProject(project);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProjectDto updateProject(@Valid @PathVariable("projectId") Integer projectId,
			@Valid @ModelAttribute("projectName") String projectName,
			@Valid @ModelAttribute("programId") Integer programId,
			@Valid @ModelAttribute("statusId") Integer statusId) {
		Project project = projectService.update(projectId, projectName, programId, statusId);
		return convertProject(project);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "{projectId}")
	public void deleteProject(@Valid @PathVariable("projectId") Integer projectId) {
		projectService.delete(projectId);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping
	public List<ProjectDto> getAllProjects() {
		List<Project> projects = projectService.getAll();
		List<ProjectDto> projectsDto = projects.stream()
				.map(project -> convertProject(project))
				.collect(Collectors.toList());
		return projectsDto;
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "get-all-projects-by-programid/{programId}")
	public List<ProjectDto> getAllProjectsByProgramId(@Valid @PathVariable("programId") Integer programId) {
		List<Project> projects = projectService.getAllProgramId(programId);
		List<ProjectDto> projectsDto = projects.stream()
				.map(project -> convertProject(project))
				.collect(Collectors.toList());
		return projectsDto;
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "projectname/{projectName}")
	public ProjectDto getProject(@Valid @PathVariable("projectName") String projectName) {
		Project project = projectService.getProjectByName(projectName);
		return convertProject(project);
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "link-capability/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProjectDto linkCapability(@ModelAttribute("projectId") Integer projectId, 
			@ModelAttribute("capabilityId") Integer capabilityId) {
		Project project = projectService.addCapability(projectId, capabilityId);
		return convertProject(project);
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "unlink-capability/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void unlinkCapability(@ModelAttribute("projectId") Integer projectId, 
			@ModelAttribute("capabilityId") Integer capabilityId) {
		projectService.deleteCapability(projectId, capabilityId);
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "get-capabilities/{projectId}")
	public List<CapabilityDto> getCapabilities(@PathVariable("projectId") Integer projectId) {
		Set<Capability> capabilities = projectService.getAllCapabilitiesByProjectId(projectId);
		List<CapabilityDto> capabilitiesDto = capabilities.stream()
				.map(capability -> convertCapability(capability))
				.collect(Collectors.toList());
		return capabilitiesDto;
	}
	
	private ProjectDto convertProject(Project project) {
		ProgramDto program = new ProgramDto(project.getProgram().getProgramId(), project.getProgram().getProgramName());
		StatusDto status = new StatusDto(project.getStatus().getStatusId(), project.getStatus().getValidityPeriod());
		return new ProjectDto(project.getProjectId(), project.getProjectName(), program, status);
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
