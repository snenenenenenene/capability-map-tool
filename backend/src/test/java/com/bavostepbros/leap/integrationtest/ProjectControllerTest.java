package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.dto.ProjectDto;
import com.bavostepbros.leap.domain.service.projectservice.ProjectService;
import com.bavostepbros.leap.persistence.ProgramDAL;
import com.bavostepbros.leap.persistence.ProjectDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private StatusDAL statusDAL;
	
	@Autowired
	private ProgramDAL programDAL;
	
	@Autowired
	private ProjectDAL projectDAL;
	
	@Autowired
	private ProjectService projectService;
	
	private Status statusFirst;
	private Status statusSecond;
	private Program programFirst;
	private Program programSecond;
	private Project projectFirst;
	private Project projectSecond;
	private Project projectThirth;
	
	static final String PATH = "/api/project/";
	
	@BeforeEach
	public void init() {
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 05, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 05, 20)));
		programFirst = programDAL.save(new Program(1, "Program 1"));
		programSecond = programDAL.save(new Program(2, "Program 2"));
		projectFirst = projectDAL.save(new Project(1, "Project 1", programFirst, statusFirst));
		projectSecond = projectDAL.save(new Project(2, "Project 2", programFirst, statusFirst));
		projectThirth = projectDAL.save(new Project(3, "Project 3", programSecond, statusSecond));
	}
	
	@AfterEach
	public void close() {
		statusDAL.delete(statusFirst);
		statusDAL.delete(statusSecond);
		programDAL.delete(programFirst);
		programDAL.delete(programSecond);
		projectDAL.delete(projectFirst);
		projectDAL.delete(projectSecond);
		projectDAL.delete(projectThirth);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusDAL);
		assertNotNull(programDAL);
		assertNotNull(projectDAL);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(programFirst);
		assertNotNull(programSecond);
		assertNotNull(projectFirst);
		assertNotNull(projectSecond);
		assertNotNull(projectThirth);
	}
	
	@Test
	public void should_postProject_whenSaveProject() throws Exception {	
		String projectName = "abc";
		Integer programId = programSecond.getProgramId();
		Integer statusId = statusSecond.getStatusId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("projectName", projectName)
				.param("programId", programId.toString())
				.param("statusId", statusId.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ProjectDto projectDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ProjectDto.class);
		
		Project project = projectService.getProjectByName(projectName);
		
		assertNotNull(projectDto);
		testProject(project, projectDto);
	}
	
	@Test
	public void should_getProject_whenGetProjectById() throws Exception {
		Integer projectId = projectFirst.getProjectId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + projectId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ProjectDto projectDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ProjectDto.class);
		
		assertNotNull(projectDto);
		testProject(projectFirst, projectDto);
	}
	
	@Test
	public void should_putProject_whenUpdateProject() throws Exception {	
		Integer projectId = projectFirst.getProjectId();
		String projectName = "def";
		Integer programId = projectFirst.getProgram().getProgramId();
		Integer statusId = projectFirst.getStatus().getStatusId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PATH + projectId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("projectName", projectName)
				.param("programId", programId.toString())
				.param("statusId", statusId.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ProjectDto projectDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ProjectDto.class);
		
		Project project = projectService.getProjectByName(projectName);
		
		assertNotNull(projectDto);
		testProject(project, projectDto);
	}
	
	@Test
	public void should_deleteProject_whenDeleteProject() throws Exception {
		Integer projectId = projectFirst.getProjectId();
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + projectId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_getProjects_whenGetAllProjects() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<ProjectDto> projectDtos = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<ProjectDto>>() {});
		
		assertNotNull(projectDtos);
		testProject(projectFirst, projectDtos.get(0));
		testProject(projectSecond, projectDtos.get(1));
		testProject(projectThirth, projectDtos.get(2));
	}
	
	@Test
	public void should_getProjects_whenGetAllProjectsByProgramId() throws Exception {
		Integer programId = projectFirst.getProgram().getProgramId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "get-all-projects-by-programid/" + programId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<ProjectDto> projectDtos = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<ProjectDto>>() {});
		
		assertNotNull(projectDtos);
		testProject(projectFirst, projectDtos.get(0));
		testProject(projectSecond, projectDtos.get(1));
	}
	
	@Test
	public void should_getProject_whenGetProjectByName() throws Exception {
		String projectName = projectSecond.getProjectName();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "projectname/" + projectName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ProjectDto projectDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ProjectDto.class);
		
		assertNotNull(projectDto);
		testProject(projectSecond, projectDto);
	}
	
	@Test
	private void testProject(Project expectedObject, ProjectDto actualObject) {
		assertEquals(expectedObject.getStatus().getStatusId(), actualObject.getStatus().getStatusId());
		assertEquals(expectedObject.getStatus().getValidityPeriod(), actualObject.getStatus().getValidityPeriod());
		
		assertEquals(expectedObject.getProgram().getProgramId(), actualObject.getProgram().getProgramId());
		assertEquals(expectedObject.getProgram().getProgramName(), actualObject.getProgram().getProgramName());
		
		assertEquals(expectedObject.getProjectId(), actualObject.getProjectId());
		assertEquals(expectedObject.getProjectName(), actualObject.getProjectName());
	}
}
