package com.bavostepbros.leap.domain.service.capabilityservice;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.customexceptions.CapabilityException;
import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.EnumException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.projectservice.ProjectService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.CapabilityDAL;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Bavo Van Meel
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CapabilityServiceImpl implements CapabilityService {

	@Autowired
	private CapabilityDAL capabilityDAL;

	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	private StatusService statusService;
	
	@Autowired
	private ProjectService projectService;

	@Override
	public Capability save(Integer environmentId, Integer statusId, Integer parentCapabilityId, String capabilityName,
							  boolean paceOfChange, String targetOperatingModel, Integer resourceQuality,
							  Integer informationQuality, Integer applicationFit) {
		if (capabilityName == null || capabilityName.isBlank() || capabilityName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (environmentId == null || environmentId.equals(0)) {
			throw new ForeignKeyException("Environment ID is invalid.");
		}
		if (statusId == null || statusId.equals(0)) {
			throw new ForeignKeyException("Status ID is invalid.");
		}
		if (existsByCapabilityName(capabilityName)) {
			throw new DuplicateValueException("Capability name already exists.");
		}
		if (!statusService.existsById(statusId)) {
			throw new ForeignKeyException("Status ID does not exists.");
		}
		if (!environmentService.existsById(environmentId)) {
			throw new ForeignKeyException("Environment ID does not exists.");
		}

		Environment environment = environmentService.get(environmentId);
		Status status = statusService.get(statusId);
		Capability capability = new Capability(environment, status, parentCapabilityId, capabilityName,
				paceOfChange, targetOperatingModel, resourceQuality, informationQuality, applicationFit);
		updateLevel(capability);

		Capability savedCapability = capabilityDAL.save(capability);
		environmentService.addCapability(environmentId, savedCapability);
		return savedCapability;
	}

	@Override
	public Capability get(Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Capability ID is not valid.");
		}
		if (!existsById(id)) {
			throw new IndexDoesNotExistException("Capability ID does not exists.");
		}
		
		Capability capability = capabilityDAL.findById(id).get();;
		return capability;
	}

	@Override
	public List<Capability> getAll() {
		return capabilityDAL.findAll();
	}

	@Override
	public Capability update(Integer capabilityId, Integer environmentId, Integer statusId, Integer parentCapabilityId,
			String capabilityName, boolean paceOfChange, String targetOperatingModel,
			Integer resourceQuality, Integer informationQuality, Integer applicationFit) {
		if (capabilityId == null || capabilityId.equals(0) || capabilityName == null || capabilityName.isBlank()
				|| capabilityName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (environmentId == null || environmentId.equals(0)) {
			throw new ForeignKeyException("Environment ID is invalid.");
		}
		if (statusId == null || statusId.equals(0)) {
			throw new ForeignKeyException("Status ID is invalid.");
		}
		if (!existsById(capabilityId)) {
			throw new CapabilityException("Can not update capability if it does not exist.");
		}
		Capability oldCapability = capabilityDAL.findById(capabilityId).get();
		if (capabilityName != oldCapability.getCapabilityName() && existsByCapabilityName(capabilityName)) {
			throw new DuplicateValueException("Capability name already exists.");
		}
		if (!statusService.existsById(statusId)) {
			throw new ForeignKeyException("Status ID does not exists.");
		}
		if (!environmentService.existsById(environmentId)) {
			throw new ForeignKeyException("Environment ID does not exists.");
		}

		Environment environment = environmentService.get(environmentId);
		Status status = statusService.get(statusId);
		Capability capability = new Capability(capabilityId, environment, status, parentCapabilityId, capabilityName,
				paceOfChange, targetOperatingModel, resourceQuality, informationQuality, applicationFit);
		updateLevel(capability);
		Capability updatedCapability = capabilityDAL.save(capability);
		return updatedCapability;
	}

	//TODO try catch for out of bounds exception
	@Override
	public void updateLevel(Capability capability) {
		if(capability.getParentCapabilityId() == 0)
			capability.setLevel(CapabilityLevel.ONE);
		else
		 	capability.setLevel(capabilityDAL.getOne(capability.getParentCapabilityId()).getLevel().next());
	}

	// TODO write unit tests!
	@Override
	public void delete(Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Capability ID is not valid.");
		}
		if (!existsById(id)) {
			throw new IndexDoesNotExistException("Capability ID does not exists.");
		}

		Capability capability = get(id);
		CapabilityLevel capabilityLevel = capability.getLevel();
		for (int i = CapabilityLevel.getMax(); i >= capabilityLevel.getLevel(); i--) {
			if (i > capabilityLevel.getLevel()) {
				capabilityDAL.deleteByParentCapabilityIdAndLevel(id, CapabilityLevel.getValue(i));
			} else {
				capabilityDAL.deleteById(id);
			}
		}
	}

	@Override
	public List<Capability> getCapabilitiesByEnvironment(Integer environmentId) {
		if (environmentId == null || environmentId.equals(0)) {
			throw new InvalidInputException("Environment ID is not valid.");
		}
		if (!environmentService.existsById(environmentId)) {
			throw new ForeignKeyException("Environment ID does not exists.");
		}
		
		Environment environment = environmentService.get(environmentId);
		List<Capability> capabilities = capabilityDAL.findByEnvironment(environment);
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilitiesByLevel(String level) {
		if (level == null) {
			throw new InvalidInputException("CapabilityLevel is not valid.");
		}
		if (Arrays.stream(CapabilityLevel.values())
				.noneMatch((capLevel) -> capLevel.name().equals(level))) { 
			throw new EnumException("CapabilityLevel is not valid."); 
		}
				
		CapabilityLevel capabilityLevel = CapabilityLevel.valueOf(level);	
		List<Capability> capabilities = capabilityDAL.findByLevel(capabilityLevel);
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilityChildren(Integer parentId) {
		if (parentId == null || parentId.equals(0)) {
			throw new InvalidInputException("Parent ID is not valid.");
		}
		if (!existsById(parentId)) {
			throw new IndexDoesNotExistException("Parent ID does not exists.");
		}
		List<Capability> capabilities = capabilityDAL.findByParentCapabilityId(parentId);
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilitiesByParentIdAndLevel(Integer parentId, String level) {
		if (parentId == null || parentId.equals(0)) {
			throw new InvalidInputException("Parent ID is not valid.");
		}
		if (level == null) {
			throw new InvalidInputException("CapabilityLevel is not valid.");
		}
		if (!existsById(parentId)) {
			throw new IndexDoesNotExistException("Parent ID does not exists.");
		}
		if (Arrays.stream(CapabilityLevel.values())
				.noneMatch((capLevel) -> capLevel.name().equals(level))) { 
			throw new EnumException("CapabilityLevel is not valid."); 
		}
				
		CapabilityLevel capabilityLevel = CapabilityLevel.valueOf(level);
		List<Capability> capabilities = capabilityDAL.findByParentCapabilityIdAndLevel(parentId, capabilityLevel);
		return capabilities;
	}

	@Override
	public boolean existsById(Integer id) {
		return capabilityDAL.existsById(id);
	}

	@Override
	public boolean existsByCapabilityName(String capabilityName) {
		return !capabilityDAL.findByCapabilityName(capabilityName).isEmpty();
	}
	
	// TODO write unit tests for this one!
	@Override
	public Capability getCapabilityByCapabilityName(String capabilityName) {
		if (capabilityName == null || capabilityName.isBlank() || capabilityName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		
		return capabilityDAL.findByCapabilityName(capabilityName).get();
	}

	@Override
	public void addProject(Integer capabilityId, Integer projectId) {
		Capability capability = get(capabilityId);
		Project project = projectService.get(projectId);
		capability.addProject(project);
		return;
	}

	@Override
	public void deleteProject(Integer capabilityId, Integer projectId) {
		Capability capability = get(capabilityId);
		Project project = projectService.get(projectId);
		capability.removeProject(project);
	}

	@Override
	public List<Project> getAllProjectsByCapabilityId(Integer capabilityId) {
		Capability capability = get(capabilityId);
		return capability.getProjects();
	}

}
