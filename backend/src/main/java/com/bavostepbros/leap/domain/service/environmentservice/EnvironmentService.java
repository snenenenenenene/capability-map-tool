package com.bavostepbros.leap.domain.service.environmentservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Environment;

/**
*
* @author Bavo Van Meel
*
*/
public interface EnvironmentService {
	Environment save(String environmentName);
	Environment get(Integer id);
	Environment getByEnvironmentName(String evironmentName);
	List<Environment> getAll();
	Environment update(Integer environmentId, String evironmentName);
	void delete(Integer id);
	boolean existsById(Integer id);
	boolean existsByEnvironmentName(String environmentName);
}
