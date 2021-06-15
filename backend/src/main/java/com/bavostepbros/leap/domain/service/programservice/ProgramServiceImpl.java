package com.bavostepbros.leap.domain.service.programservice;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.persistence.ProgramDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {
	
	@Autowired
    private ProgramDAL programDAL;

	
	/** 
	 * @param programName
	 * @return Program
	 */
	@Override
	public Program save(String programName) {
		Program program = new Program(programName);
		return programDAL.save(program);
	}

	
	/** 
	 * @param programId
	 * @return Program
	 */
	@Override
	public Program get(Integer programId) {
		Optional<Program> program = programDAL.findById(programId);
		program.orElseThrow(() -> new NullPointerException("Program does not exist."));
		return program.get();
	}

	
	/** 
	 * @param programId
	 * @param programName
	 * @return Program
	 */
	@Override
	public Program update(Integer programId, String programName) {
		Program program = new Program(programId, programName);
		return programDAL.save(program);
	}

	
	/** 
	 * @param programId
	 */
	@Override
	public void delete(Integer programId) {
		programDAL.deleteById(programId);
	}

	
	/** 
	 * @return List<Program>
	 */
	@Override
	public List<Program> getAll() {
		return programDAL.findAll();
	}

	
	/** 
	 * @param programName
	 * @return Program
	 */
	@Override
	public Program getByProgramName(String programName) {
		Optional<Program> program = programDAL.findByProgramName(programName);
		program.orElseThrow(() -> new NullPointerException("Program does not exist."));
		return program.get();
	}

}
