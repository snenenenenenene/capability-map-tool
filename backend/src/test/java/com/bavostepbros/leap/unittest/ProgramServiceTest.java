package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.bavostepbros.leap.domain.service.programservice.ProgramService;
import com.bavostepbros.leap.persistence.ProgramDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class ProgramServiceTest {
	
	@Autowired
	private ProgramService programService;
	
	@MockBean
	private ProgramDAL programDAL;
	
	private Program programFirst;
	private Program programSecond;
	private List<Program> programs;
	private Optional<Program> optionalProgramFirst;
	
	private Integer programId;
	private String programName;
	
	@BeforeEach
	void init() {
		programFirst = new Program(1, "Program 1");
		programSecond = new Program(2, "Program 2");
		programs = List.of(programFirst, programSecond);
		optionalProgramFirst = Optional.of(programFirst);
		
		programId = programFirst.getProgramId();
		programName = programFirst.getProgramName();
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(programService);
		assertNotNull(programDAL);
		
		assertNotNull(programFirst);
		assertNotNull(programSecond);
		assertNotNull(programs);
		assertNotNull(optionalProgramFirst);
	}
	
	@Test 
	void should_returnProgram_whenSaveProgram() {
		BDDMockito.given(programDAL.save(BDDMockito.any(Program.class))).willReturn(programFirst);
		
		Program program = programService.save(programName);
		
		assertNotNull(program);
		assertTrue(program instanceof Program);
		testProgram(programFirst, program);
	}
	
	@Test 
	void should_throwNullPointerException_whenGetProgramByIdInvalidId() {
		String expectedErrorMessage = "Program does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> programService.get(programId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnProgram_whenGetProgramById() {
		BDDMockito.given(programDAL.findById(BDDMockito.anyInt())).willReturn(optionalProgramFirst);
		
		Program program = programService.get(programId);
		
		assertNotNull(program);
		assertTrue(program instanceof Program);
		testProgram(programFirst, program);
	}
	
	@Test 
	void should_returnProgram_whenUpdateProgram() {
		BDDMockito.given(programDAL.save(BDDMockito.any(Program.class))).willReturn(programFirst);
		
		Program program = programService.update(programId, programName);
		
		assertNotNull(program);
		assertTrue(program instanceof Program);
		testProgram(programFirst, program);
	}
	
	@Test 
	void should_verifyDeleted_whenDeleteProgram() {
		programService.delete(programId);
		
		Mockito.verify(programDAL, Mockito.times(1)).deleteById(Mockito.eq(programId));
	}
	
	@Test 
	void should_returnPrograms_whenGetAllProgram() {
		BDDMockito.given(programDAL.findAll()).willReturn(programs);
		
		List<Program> programsResult = programService.getAll();
		
		assertNotNull(programsResult);
		assertEquals(programs.size(), programsResult.size());
		testProgram(programFirst, programsResult.get(0));
		testProgram(programSecond, programsResult.get(1));
	}
	
	@Test 
	void should_throwNullPointerException_whenGetProgramByIdInvalidName() {
		String expectedErrorMessage = "Program does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> programService.getByProgramName(programName));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnProgram_whenGetProgramByName() {
		BDDMockito.given(programDAL.findByProgramName(BDDMockito.anyString())).willReturn(optionalProgramFirst);
		
		Program program = programService.getByProgramName(programName);
		
		assertNotNull(program);
		assertTrue(program instanceof Program);
		testProgram(programFirst, program);
	}
	
	private void testProgram(Program expectedObject, Program actualObject) {
		assertEquals(expectedObject.getProgramId(), actualObject.getProgramId());
		assertEquals(expectedObject.getProgramName(), actualObject.getProgramName());
	}
}
