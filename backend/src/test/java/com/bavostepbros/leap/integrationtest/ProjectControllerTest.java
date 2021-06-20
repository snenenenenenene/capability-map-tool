package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.ProjectDto;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.projectservice.ProjectService;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.ProgramDAL;
import com.bavostepbros.leap.persistence.ProjectDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectControllerTest extends ApiIntegrationTest {
	
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
	private EnvironmentDAL environmentDAL;
	
	@Autowired
	private CapabilityDAL capabilityDAL;
	
	@Autowired
	private CapabilityService capabilityService;
	
	@Autowired
	private ProjectService projectService;
	
	private Status statusFirst;
	private Status statusSecond;
	private Program programFirst;
	private Program programSecond;
	private Project projectFirst;
	private Project projectSecond;
	private Project projectThirth;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Capability capabilityFirst;
	private Capability capabilitySecond;
	
	static final String PATH = "/api/project/";

	@BeforeAll
	public void authenticate() throws Exception { super.authenticate(); }

	@BeforeEach
	public void init() {
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 05, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 05, 20)));
		programFirst = programDAL.save(new Program(1, "Program 1"));
		programSecond = programDAL.save(new Program(2, "Program 2"));
		projectFirst = projectDAL.save(new Project(1, "Project 1", programFirst, statusFirst));
		projectSecond = projectDAL.save(new Project(2, "Project 2", programFirst, statusFirst));
		projectThirth = projectDAL.save(new Project(3, "Project 3", programSecond, statusSecond));
		environmentFirst = environmentDAL.save(new Environment(1, "Test 1"));
		environmentSecond = environmentDAL.save(new Environment(2, "Test 2"));
		capabilityFirst = capabilityDAL.save(new Capability(1, environmentFirst, statusFirst, 0, "Capability 1",
				"Description 1", PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 1, 1, 1));
		capabilityService.updateLevel(capabilityFirst);
		capabilitySecond = capabilityDAL.save(
				new Capability(2, environmentSecond, statusSecond, capabilityFirst.getCapabilityId(), "Capability 2",
						"Description 2", PaceOfChange.INNOVATIVE, TargetOperatingModel.DIVERSIFICATION, 1, 1, 1));
		capabilityService.updateLevel(capabilitySecond);
		projectService.addCapability(projectSecond.getProjectId(), capabilitySecond.getCapabilityId());
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
		assertNotNull(environmentDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(capabilityService);
		
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
		
		MvcResult mvcResult = mockMvc.perform(post(PATH)
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
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + projectId))
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
		
		MvcResult mvcResult = mockMvc.perform(put(PATH + projectId)
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
		
		mockMvc.perform(delete(PATH + projectId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_getProjects_whenGetAllProjects() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH))
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
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "get-all-projects-by-programid/" + programId))
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
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "projectname/" + projectName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ProjectDto projectDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ProjectDto.class);
		
		assertNotNull(projectDto);
		testProject(projectSecond, projectDto);
	}
	
	@Test
	public void should_returnOk_whenLinkCapability() throws Exception {
		Integer projectId = projectFirst.getProjectId();
		Integer capabilityId = capabilityFirst.getCapabilityId();
		
		mockMvc.perform(put(PATH + "link-capability/")
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.param("projectId", projectId.toString())
			.param("capabilityId", capabilityId.toString())
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_returnOk_whenUnlinkCapability() throws Exception {
		Integer projectId = projectFirst.getProjectId();
		Integer capabilityId = capabilityFirst.getCapabilityId();
		
		mockMvc.perform(delete(PATH + "unlink-capability/")
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.param("projectId", projectId.toString())
			.param("capabilityId", capabilityId.toString())
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_getAllCapabilities_whenGetAllCapabilitiesByProjectId() throws Exception {
		Integer projectId = projectSecond.getProjectId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "get-capabilities/" + projectId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityDto> resultCapabilities = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<List<CapabilityDto>>() {});
		
		assertNotNull(resultCapabilities);
		testCapability(capabilitySecond, resultCapabilities.get(0));
	}
	
	private void testProject(Project expectedObject, ProjectDto actualObject) {
		assertEquals(expectedObject.getStatus().getStatusId(), actualObject.getStatus().getStatusId());
		assertEquals(expectedObject.getStatus().getValidityPeriod(), actualObject.getStatus().getValidityPeriod());
		
		assertEquals(expectedObject.getProgram().getProgramId(), actualObject.getProgram().getProgramId());
		assertEquals(expectedObject.getProgram().getProgramName(), actualObject.getProgram().getProgramName());
		
		assertEquals(expectedObject.getProjectId(), actualObject.getProjectId());
		assertEquals(expectedObject.getProjectName(), actualObject.getProjectName());
	}
	
	private void testCapability(Capability expectedObject, CapabilityDto actualObject) {
		assertEquals(expectedObject.getCapabilityId(), actualObject.getCapabilityId());
		assertEquals(expectedObject.getEnvironment().getEnvironmentId(),
				actualObject.getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getEnvironment().getEnvironmentName(),
				actualObject.getEnvironment().getEnvironmentName());
		
		assertEquals(expectedObject.getStatus().getStatusId(), actualObject.getStatus().getStatusId());
		assertEquals(expectedObject.getStatus().getValidityPeriod(), actualObject.getStatus().getValidityPeriod());
		
		assertEquals(expectedObject.getParentCapabilityId(), actualObject.getParentCapabilityId());
		assertEquals(expectedObject.getCapabilityName(), actualObject.getCapabilityName());
		assertEquals(expectedObject.getLevel(), actualObject.getLevel());
		assertEquals(expectedObject.getPaceOfChange(), actualObject.getPaceOfChange());
		assertEquals(expectedObject.getTargetOperatingModel(), actualObject.getTargetOperatingModel());
		assertEquals(expectedObject.getResourceQuality(), actualObject.getResourceQuality());
		assertEquals(expectedObject.getInformationQuality(), actualObject.getInformationQuality());
		assertEquals(expectedObject.getApplicationFit(), actualObject.getApplicationFit());
	}
}
