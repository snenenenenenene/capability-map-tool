package com.bavostepbros.leap.domain.service.environmentservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.EnvironmentException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.persistence.EnvironmentDAL;

import lombok.RequiredArgsConstructor;

/**
*
* @author Bavo Van Meel
*
*/
@Service
@Transactional
@RequiredArgsConstructor
public class EnvironmentServiceImpl implements EnvironmentService {
	
	@Autowired
    private EnvironmentDAL environmentDAL;

    @Override
    public Environment save(String environmentName) {
    	if (environmentName == null || environmentName.isBlank() || environmentName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
    	if (!existsByEnvironmentName(environmentName)) {
			throw new DuplicateValueException("Environment name already exists.");
		}
		
    	Environment environment = new Environment(environmentName);
        return environmentDAL.save(environment);
    }

    @Override
    public Environment get(Integer id) {
    	if (id == null || id.equals(0)) {
			throw new InvalidInputException("Environment ID is not valid.");
		}
    	if (!existsById(id)) {
			throw new IndexDoesNotExistException("Environment ID does not exists.");
		}
    	
        Environment environment = environmentDAL.findById(id).get();
        return environment;
    }

    @Override
    public Environment getByEnvironmentName(String environmentName) {
    	if (environmentName == null || environmentName.isBlank() || environmentName.isEmpty()) {
			throw new InvalidInputException("Environment name is not valid.");
		}
    	if (existsByEnvironmentName(environmentName)) {
			throw new EnvironmentException("Environment name does not exists.");
		}
    	
    	// Nullpointer naar hier trekken zodat die niet in DAL wordt gegooid
        Optional<Environment> environment = environmentDAL.findByEnvironmentName(environmentName);
        environment.orElseThrow(() -> new NullPointerException("Environment does not exist."));
        return environment.get();
    }

    @Override
    public List<Environment> getAll() {
        List<Environment> environments = new ArrayList<Environment>();
        environments = environmentDAL.findAll();
        return environments;
    }

    @Override
    public Environment update(Integer environmentId, String environmentName) {
    	if (environmentId == null || environmentId.equals(0) ||
				environmentName.isBlank() || environmentName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
    	if (!existsById(environmentId)) {
			throw new IndexDoesNotExistException("Can not update environment if it does not exist.");
		}
		if (!existsByEnvironmentName(environmentName)) {
			throw new EnvironmentException("Environment name already exists.");
		}

    	Environment environment = new Environment(environmentId, environmentName);
    	Environment updatedEnvironment = environmentDAL.save(environment);
        return updatedEnvironment;
    }

    @Override
    public void delete(Integer id) {
    	if (id == null || id.equals(0)) {
			throw new InvalidInputException("Environment ID is not valid.");
		}
    	
        environmentDAL.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
    	return environmentDAL.existsById(id);
    }

    @Override
    public boolean existsByEnvironmentName(String environmentName) {
        return environmentDAL.findByEnvironmentName(environmentName).isEmpty();        
    }

}
