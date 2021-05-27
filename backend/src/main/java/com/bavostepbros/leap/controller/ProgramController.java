package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.domain.model.dto.ProgramDto;
import com.bavostepbros.leap.domain.service.programservice.ProgramService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/program/")
public class ProgramController {
	
	@Autowired
	private ProgramService programService;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProgramDto addProgram(@Valid @ModelAttribute("programName") String programName) {
		Program program = programService.save(programName);
		return new ProgramDto(program.getProgramId(), program.getProgramName());
	}
	
	@GetMapping(path = "{programId}")
    public ProgramDto getProgramById(@Valid @PathVariable("programId") Integer programId) {
		Program program = programService.get(programId);
		return new ProgramDto(program.getProgramId(), program.getProgramName());
	}
	
	@PutMapping(path = "{programId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProgramDto updateProgram(@Valid @PathVariable("programId") Integer programId, 
			@Valid @ModelAttribute("programName") String programName) {
		Program program = programService.update(programId, programName);
		return new ProgramDto(program.getProgramId(), program.getProgramName());
	}
	
	@DeleteMapping(path = "{programId}")
	public void deleteProgram(@Valid @PathVariable("programId") Integer programId) {
		programService.delete(programId);
	}
	
	@GetMapping
    public List<ProgramDto> getAllProgram() {
		List<Program> programs = programService.getAll();
		List<ProgramDto> programDto = programs.stream()
				.map(program -> new ProgramDto(program.getProgramId(), 
						program.getProgramName()))
				.collect(Collectors.toList());
		return programDto;
	}
	
	@GetMapping(path = "programname/{programName}")
    public ProgramDto getProgramByName(@Valid @PathVariable("programName") String programName) {
		Program program = programService.getByProgramName(programName);
		return new ProgramDto(program.getProgramId(), program.getProgramName());
	}
}
