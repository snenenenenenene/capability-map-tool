package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.model.Project;
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

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProjectDto addProject(@Valid @ModelAttribute("projectName") String projectName,
			@Valid @ModelAttribute("programId") Integer programId,
			@Valid @ModelAttribute("statusId") Integer statusId) {
		Project project = projectService.save(projectName, programId, statusId);
		return convertProject(project);
	}

	@GetMapping(path = "{projectId}")
	public ProjectDto getProject(@PathVariable("projectId") Integer projectId) {
		Project project = projectService.get(projectId);
		return convertProject(project);
	}

	@PutMapping(path = "{projectId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProjectDto updateProject(@Valid @PathVariable("projectId") Integer projectId,
			@Valid @ModelAttribute("projectName") String projectName,
			@Valid @ModelAttribute("programId") Integer programId,
			@Valid @ModelAttribute("statusId") Integer statusId) {
		Project project = projectService.update(projectId, projectName, programId, statusId);
		return convertProject(project);
	}

	@DeleteMapping(path = "{projectId}")
	public void deleteProject(@Valid @PathVariable("projectId") Integer projectId) {
		projectService.delete(projectId);
	}

	@GetMapping
	public List<ProjectDto> getAllProjects() {
		List<Project> projects = projectService.getAll();
		List<ProjectDto> projectsDto = projects.stream()
				.map(project -> convertProject(project))
				.collect(Collectors.toList());
		return projectsDto;
	}
	
	@GetMapping(path = "get-all-projects-by-programid/{programId}")
	public List<ProjectDto> getAllProjectsByProgramId(@Valid @PathVariable("programId") Integer programId) {
		List<Project> projects = projectService.getAllProgramId(programId);
		List<ProjectDto> projectsDto = projects.stream()
				.map(project -> convertProject(project))
				.collect(Collectors.toList());
		return projectsDto;
	}
	
	@GetMapping(path = "projectname/{projectName}")
	public ProjectDto getProject(@Valid @PathVariable("projectName") String projectName) {
		Project project = projectService.getProjectByName(projectName);
		return convertProject(project);
	}
	
	private ProjectDto convertProject(Project project) {
		ProgramDto program = new ProgramDto(project.getProgram().getProgramId(), project.getProgram().getProgramName());
		StatusDto status = new StatusDto(project.getStatus().getStatusId(), project.getStatus().getValidityPeriod());
		return new ProjectDto(project.getProjectId(), project.getProjectName(), program, status);
	}
}
