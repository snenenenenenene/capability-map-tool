package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.domain.model.dto.ProgramDto;
import com.bavostepbros.leap.domain.service.programservice.ProgramService;
import com.bavostepbros.leap.persistence.ProgramDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProgramControllerTest extends ApiIntegrationTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ProgramDAL programDAL;
	
	@Autowired
	private ProgramService programService;
	
	private Program programFirst;
	private Program programSecond;
	
	static final String PATH = "/api/program/";

	
	/** 
	 * @param init(
	 * @throws Exceptionpublic void init()
	 */
	@BeforeAll
	public void authenticate() throws Exception { super.authenticate(); }

	@BeforeEach
	public void init() {
		programFirst = programDAL.save(new Program(1, "Program 1"));
		programSecond = programDAL.save(new Program(2, "Program 2"));
	}
	
	@AfterEach
	public void close() {
		programDAL.delete(programFirst);
		programDAL.delete(programSecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(programDAL);
		assertNotNull(programService);
		assertNotNull(programFirst);
		assertNotNull(programSecond);
	}
	
	
	/** 
	 * @throws Exception
	 */
	/*
	 * @Test public void
	 * should_throwInvalidInputIfProgramNameIsInvalid_whenSaveProgram() throws
	 * Exception { String programName = "";
	 * 
	 * mockMvc.perform(MockMvcRequestBuilders.post(PATH)
	 * .contentType(MediaType.MULTIPART_FORM_DATA_VALUE) .param("programName",
	 * programName) .accept(MediaType.APPLICATION_JSON))
	 * .andExpect(MockMvcResultMatchers.status().isInternalServerError())
	 * .andExpect(result -> assertEquals("Program name is required",
	 * result.getResolvedException().getMessage())) .andReturn();
	 * 
	 * // assertEquals(mvcResult.getResolvedException().getMessage(),
	 * "Program name is required"); }
	 */
	
	@Test
	public void should_postProgram_whenSaveProgram() throws Exception {	
		String programName = "abc";
		
		MvcResult mvcResult = mockMvc.perform(post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("programName", programName)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ProgramDto programDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ProgramDto.class);
		
		Program program = programService.getByProgramName(programName);
		
		assertNotNull(programDto);
		testProgram(program, programDto);
	}
	
	
	/** 
	 * @throws Exception
	 */
	@Test
	public void should_getProgram_whenGetProgramById() throws Exception {	
		Integer programId = programFirst.getProgramId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + programId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ProgramDto programDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ProgramDto.class);
		
		assertNotNull(programDto);
		testProgram(programFirst, programDto);
	}
	
	
	/** 
	 * @throws Exception
	 */
	@Test
	public void should_putProgram_whenUpdateProgram() throws Exception {	
		Integer programId = programFirst.getProgramId();
		String programName = "abc";
		
		MvcResult mvcResult = mockMvc.perform(put(PATH + programId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("programName", programName)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ProgramDto programDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ProgramDto.class);
		
		Program program = programService.getByProgramName(programName);
		
		assertNotNull(programDto);
		testProgram(program, programDto);
	}
	
	
	/** 
	 * @throws Exception
	 */
	@Test
	public void should_deleteProgram_whenDeleteProgram() throws Exception {	
		Integer programId = programFirst.getProgramId();
		
		mockMvc.perform(delete(PATH + programId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	/** 
	 * @throws Exception
	 */
	@Test
	public void should_getPrograms_whenGetAllProgram() throws Exception {			
		MvcResult mvcResult = mockMvc.perform(get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<ProgramDto> programDtos = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<ProgramDto>>() {});
		
		assertNotNull(programDtos);
		assertEquals(2, programDtos.size());
		testProgram(programFirst, programDtos.get(0));
		testProgram(programSecond, programDtos.get(1));
	}
	
	
	/** 
	 * @throws Exception
	 */
	@Test
	public void should_getProgram_whenGetProgramByName() throws Exception {	
		String programName = programFirst.getProgramName();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "programname/" + programName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ProgramDto programDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ProgramDto.class);
		
		assertNotNull(programDto);
		testProgram(programFirst, programDto);
	}
	
	
	/** 
	 * @param expectedObject
	 * @param actualObject
	 */
	@Test
	private void testProgram(Program expectedObject, ProgramDto actualObject) {
		assertEquals(expectedObject.getProgramId(), actualObject.getProgramId());
		assertEquals(expectedObject.getProgramName(), actualObject.getProgramName());
	}
}
