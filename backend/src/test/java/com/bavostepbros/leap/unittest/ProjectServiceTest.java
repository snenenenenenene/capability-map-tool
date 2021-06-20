package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.service.projectservice.ProjectService;
import com.bavostepbros.leap.persistence.ProgramDAL;
import com.bavostepbros.leap.persistence.ProjectDAL;
import com.bavostepbros.leap.persistence.StatusDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class ProjectServiceTest {
	
	@Autowired
	private ProjectService projectService;
	
	@MockBean
	private ProjectDAL projectDAL;
	
	@MockBean
	private ProgramDAL programDAL;
	
	@MockBean
	private StatusDAL statusDAL;
	
	private Program programFirst;
	private Program programSecond;
	private Status statusFirst;
	private Status statusSecond;
	private Project projectFirst;
	private Project projectSecond;
	private List<Project> projects;
	private Optional<Program> optionalProgramFirst;
	private Optional<Status> optionalStatusFirst;
	private Optional<Project> optionalProjectFirst;
	
	private Integer projectId;
	private String projectName;
	private Integer programId;
	private Integer statusId;
	
	@BeforeEach
	void init() {
		programFirst = new Program(1, "Program 1");
		programSecond = new Program(2, "Program 2");
		statusFirst = new Status(1, LocalDate.of(2021, 05, 15));
		statusSecond = new Status(2, LocalDate.of(2021, 10, 10));
		projectFirst = new Project(1, "Project 1", programFirst, statusFirst);
		projectSecond = new Project(2, "Project 2", programSecond, statusSecond);
		projects = List.of(projectFirst, projectSecond);
		optionalProgramFirst = Optional.of(programFirst);
		optionalStatusFirst = Optional.of(statusFirst);
		optionalProjectFirst = Optional.of(projectFirst);
		
		projectId = projectFirst.getProjectId();
		projectName = projectFirst.getProjectName();
		programId = projectFirst.getProgram().getProgramId();
		statusId = projectFirst.getStatus().getStatusId();
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(projectService);
		assertNotNull(projectDAL);
		assertNotNull(programDAL);
		assertNotNull(statusDAL);
		
		assertNotNull(programFirst);
		assertNotNull(programSecond);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(projectFirst);
		assertNotNull(projectSecond);
		assertNotNull(projects);
		assertNotNull(optionalProgramFirst);
		assertNotNull(optionalStatusFirst);
		assertNotNull(optionalProjectFirst);
		
		assertNotNull(projectId);
		assertNotNull(projectName);
		assertNotNull(programId);
		assertNotNull(statusId);
	}
	
	@Test 
	void should_throwNullPointerException_whenSaveProjectInvalidProgramId() {
		String expectedErrorMessage = "Program does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> projectService.save(projectName, programId, statusId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_throwNullPointerException_whenSaveProjectInvalidStatusId() {
		BDDMockito.given(programDAL.findById(BDDMockito.anyInt())).willReturn(optionalProgramFirst);
		String expectedErrorMessage = "Status does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> projectService.save(projectName, programId, statusId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnProject_whenSaveProject() {
		BDDMockito.given(programDAL.findById(BDDMockito.anyInt())).willReturn(optionalProgramFirst);
		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatusFirst);
		BDDMockito.given(projectDAL.save(BDDMockito.any(Project.class))).willReturn(projectFirst);
		
		Project project = projectService.save(projectName, programId, statusId);
		
		assertNotNull(project);
		assertTrue(project instanceof Project);
		testProject(projectFirst, project);
	}
	
	@Test 
	void should_throwNullPointerException_whenGetProjectByIdInvalidProgramId() {
		String expectedErrorMessage = "Project does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> projectService.get(projectId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnProject_whenGetProjectById() {
		BDDMockito.given(projectDAL.findById(BDDMockito.anyInt())).willReturn(optionalProjectFirst);
		
		Project project = projectService.get(projectId);
		
		assertNotNull(project);
		assertTrue(project instanceof Project);
		testProject(projectFirst, project);
	}
	
	@Test 
	void should_throwNullPointerException_whenUpdateProjectInvalidProgramId() {
		String expectedErrorMessage = "Program does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> projectService.update(projectId, projectName, programId, statusId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_throwNullPointerException_whenUpdateProjectInvalidStatusId() {
		BDDMockito.given(programDAL.findById(BDDMockito.anyInt())).willReturn(optionalProgramFirst);
		String expectedErrorMessage = "Status does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> projectService.update(projectId, projectName, programId, statusId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnProject_whenUpdateProject() {
		BDDMockito.given(programDAL.findById(BDDMockito.anyInt())).willReturn(optionalProgramFirst);
		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatusFirst);
		BDDMockito.given(projectDAL.save(BDDMockito.any(Project.class))).willReturn(projectFirst);
		
		Project project = projectService.update(projectId, projectName, programId, statusId);
		
		assertNotNull(project);
		assertTrue(project instanceof Project);
		testProject(projectFirst, project);
	}
	
	@Test 
	void should_verifyDeleted_whenDeleteProject() {
		projectService.delete(projectId);
		
		Mockito.verify(projectDAL, Mockito.times(1)).deleteById(Mockito.eq(projectId));
	}
	
	@Test 
	void should_returnProjects_whenGetAllProject() {
		BDDMockito.given(projectDAL.findAll()).willReturn(projects);
		
		List<Project> projectsResult = projectService.getAll();
		
		assertNotNull(projectsResult);
		assertEquals(projects.size(), projectsResult.size());
		testProject(projectFirst, projectsResult.get(0));
		testProject(projectSecond, projectsResult.get(1));
	}
	
	@Test 
	void should_throwNullPointerException_whenGetProjectByProgramIdInvalidProgramId() {
		String expectedErrorMessage = "Program does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> projectService.getAllProgramId(programId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnProjects_whenGetProjectByProgramId() {
		BDDMockito.given(programDAL.findById(BDDMockito.anyInt())).willReturn(optionalProgramFirst);
		BDDMockito.given(projectDAL.findByProgram(BDDMockito.any(Program.class))).willReturn(projects);
		
		List<Project> projectsResult = projectService.getAllProgramId(programId);
		
		assertNotNull(projectsResult);
		assertEquals(projects.size(), projectsResult.size());
		testProject(projectFirst, projectsResult.get(0));
		testProject(projectSecond, projectsResult.get(1));
	}
	
	@Test 
	void should_throwNullPointerException_whenGetProjectByNameInvalidName() {
		String expectedErrorMessage = "Project does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> projectService.getProjectByName(projectName));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnProject_whenGetProjectByName() {
		BDDMockito.given(projectDAL.findByProjectName(BDDMockito.anyString())).willReturn(optionalProjectFirst);
		
		Project project = projectService.getProjectByName(projectName);
		
		assertNotNull(project);
		assertTrue(project instanceof Project);
		testProject(projectFirst, project);
	}
	
	private void testProject(Project expectedObject, Project actualObject) {
		assertEquals(expectedObject.getProjectId(), actualObject.getProjectId());
		assertEquals(expectedObject.getProjectName(), actualObject.getProjectName());
		assertEquals(expectedObject.getProgram().getProgramId(), actualObject.getProgram().getProgramId());
		assertEquals(expectedObject.getStatus().getStatusId(), actualObject.getStatus().getStatusId());
	}
}
