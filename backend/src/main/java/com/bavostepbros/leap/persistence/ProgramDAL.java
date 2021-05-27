package com.bavostepbros.leap.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Program;

public interface ProgramDAL extends JpaRepository<Program, Integer> {
	Optional<Program> findByProgramName(String programName);
}
