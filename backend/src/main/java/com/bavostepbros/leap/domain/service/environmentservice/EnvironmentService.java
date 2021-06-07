package com.bavostepbros.leap.domain.service.environmentservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Strategy;

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
	Environment addCapability(Integer id, Capability capability);
	Environment addCapabilities(Integer id, List<Capability> capabilities);
	Environment addStrategy(Integer id, Strategy strategy);
	void delete(Integer id);
	boolean existsById(Integer id);
	boolean existsByEnvironmentName(String environmentName);
}
