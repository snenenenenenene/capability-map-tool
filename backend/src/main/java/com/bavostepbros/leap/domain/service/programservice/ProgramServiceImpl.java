package com.bavostepbros.leap.domain.service.programservice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.persistence.ProgramDAL;

public class ProgramServiceImpl implements ProgramService {
	
	@Autowired
    private ProgramDAL programDAL;

	@Override
	public Program save(String programName) {
		Program program = new Program(programName);
		return programDAL.save(program);
	}

	@Override
	public Program get(Integer programId) {
		Optional<Program> program = programDAL.findById(programId);
		program.orElseThrow(() -> new NullPointerException("Program does not exist."));
		return program.get();
	}

	@Override
	public Program update(Integer programId, String programName) {
		Program program = new Program(programId, programName);
		return programDAL.save(program);
	}

	@Override
	public void delete(Integer programId) {
		programDAL.deleteById(programId);
	}

	@Override
	public List<Program> getAll() {
		return programDAL.findAll();
	}

	@Override
	public Program getByProgramName(String programName) {
		Optional<Program> program = programDAL.findByProgramName(programName);
		program.orElseThrow(() -> new NullPointerException("Program does not exist."));
		return program.get();
	}

}
