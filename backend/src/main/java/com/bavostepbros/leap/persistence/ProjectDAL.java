package com.bavostepbros.leap.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.domain.model.Project;

public interface ProjectDAL extends JpaRepository<Project, Integer> {
	List<Project> findByProgram(Program program);

	Optional<Project> findByProjectName(String projectName);
	
}
