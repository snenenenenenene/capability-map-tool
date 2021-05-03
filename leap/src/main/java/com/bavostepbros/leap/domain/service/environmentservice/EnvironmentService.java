package com.bavostepbros.leap.domain.service.environmentservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Environment;

public interface EnvironmentService {
	boolean save(Environment environment);
	Environment get(Integer id);
	Environment getByEnvironmentName(String evironmentName);
	List<Environment> getAll();
	void update(Environment environment);
	void delete(Integer id);
	boolean existsById(Integer id);
	boolean existsByEnvironmentName(String environmentName);
}
