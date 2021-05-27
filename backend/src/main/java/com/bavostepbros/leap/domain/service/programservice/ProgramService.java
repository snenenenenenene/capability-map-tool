package com.bavostepbros.leap.domain.service.programservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Program;

public interface ProgramService {
	Program save(String programName);
	Program get(Integer programId);
	Program update(Integer programId, String programName);
	void delete(Integer programId);
	List<Program> getAll();
	Program getByProgramName(String programName);
}
