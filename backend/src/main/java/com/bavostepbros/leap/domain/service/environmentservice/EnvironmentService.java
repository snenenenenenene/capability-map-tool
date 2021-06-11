package com.bavostepbros.leap.domain.service.environmentservice;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Strategy;

/**
*
* @author Bavo Van Meel
*
*/
public interface EnvironmentService {
	Environment save(@NotBlank String environmentName);
	Environment get(Integer id);
	Environment getByEnvironmentName(@NotBlank String evironmentName);
	List<Environment> getAll();
	Environment update(Integer environmentId, @NotBlank String evironmentName);
	Environment addCapability(Integer id, Capability capability);
	Environment addCapabilities(Integer id, List<Capability> capabilities);
	Environment addStrategy(Integer id, Strategy strategy);
	void delete(Integer id);
	boolean existsById(Integer id);
	boolean existsByEnvironmentName(@NotBlank String environmentName);
}
