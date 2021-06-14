package com.bavostepbros.leap.domain.service.environmentservice;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.Id;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
@Validated
@RequiredArgsConstructor
public class EnvironmentServiceImpl implements EnvironmentService {

	@Autowired
    private EnvironmentDAL environmentDAL;

    @Override
    public Environment save(@NotBlank String environmentName) {
    	Environment environment = new Environment(environmentName);
        return environmentDAL.save(environment);
    }

    @Override
    public Environment get(Integer id) {
        Optional<Environment> environment = environmentDAL.findById(id);
        environment.orElseThrow(() -> new NullPointerException("Environment does not exist."));
        return environment.get();
    }

    @Override
    public Environment getByEnvironmentName(@NotBlank String environmentName) {
        Optional<Environment> environment = environmentDAL.findByEnvironmentName(environmentName);
        environment.orElseThrow(() -> new NullPointerException("Environment does not exist."));
        return environment.get();
    }

    @Override
    public List<Environment> getAll() {
        return environmentDAL.findAll();
    }



    @Override
    public Environment update(Integer environmentId, @NotBlank String environmentName) {
    	Environment environment = new Environment(environmentId, environmentName);
        return environmentDAL.save(environment);
    }

    @Override
    public Environment addCapability(Integer id, Capability capability) {
        Environment environment = this.get(id);
        environment.getCapabilities().add(capability);
        return environmentDAL.save(environment);
    }

    @Override
    public Environment addCapabilities(Integer id, List<Capability> capabilities) {
        Environment environment = this.get(id);
        environment.getCapabilities().addAll(capabilities);
        return environmentDAL.save(environment);
    }

    @Override
    public Environment addStrategy(Integer id, Strategy strategy) {
        Environment environment = this.get(id);
        environment.getStrategies().add(strategy);
        return environmentDAL.save(environment);
    }

    @Override
    public void delete(Integer id) {
        environmentDAL.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
    	return environmentDAL.existsById(id);
    }

    @Override
    public boolean existsByEnvironmentName(String environmentName) {
        return !environmentDAL.findByEnvironmentName(environmentName).isEmpty();        
    }

}
