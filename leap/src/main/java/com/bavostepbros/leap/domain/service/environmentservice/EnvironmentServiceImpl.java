package com.bavostepbros.leap.domain.service.environmentservice;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.persistence.EnvironmentDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EnvironmentServiceImpl implements EnvironmentService {
	
	@Autowired
    private EnvironmentDAL environmentDAL;

    @Override
    public Environment save(String environmentName) {
    	Environment environment = new Environment(environmentName);
        Environment savedEnvironment = environmentDAL.save(environment);
        return savedEnvironment;
    }

    @Override
    public Environment get(Integer id) {
        Environment environment = environmentDAL.findById(id).get();
        return environment;
    }

    @Override
    public Environment getByEnvironmentName(String evironmentName) {
        Environment environment = environmentDAL.findByEnvironmentName(evironmentName).get(0);
        return environment;
    }

    @Override
    public List<Environment> getAll() {
        List<Environment> environments = new ArrayList<Environment>();
        environments = environmentDAL.findAll();
        return environments;
    }

    @Override
    public Environment update(Integer environmentId, String evironmentName) {
    	Environment environment = new Environment(environmentId, evironmentName);
    	Environment updatedEnvironment = environmentDAL.save(environment);
        return updatedEnvironment;
    }

    @Override
    public void delete(Integer id) {
        environmentDAL.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        boolean result = environmentDAL.existsById(id);
        return result;
    }

    @Override
    public boolean existsByEnvironmentName(String environmentName) {
        boolean result = environmentDAL.findByEnvironmentName(environmentName).isEmpty();
        return result;
    }

}
