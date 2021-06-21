package com.bavostepbros.leap.domain.service.capabilityservice;

import java.util.*;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bavostepbros.leap.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.customexceptions.EnumException;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.businessprocessservice.BusinessProcessService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.projectservice.ProjectService;
import com.bavostepbros.leap.domain.service.resourceservice.ResourceService;
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

	@Autowired
	private BusinessProcessService businessProcessService;

	@Autowired
	private ResourceService resourceService;

	@Override
	public Capability save( Integer environmentId, Integer statusId,
			Integer parentCapabilityId, @NotBlank String capabilityName, String capabilityDescription,
			String paceOfChange, String targetOperatingModel, Integer resourceQuality, Integer informationQuality,
			Integer applicationFit) {
		return save(new Capability(environmentService.get(environmentId), statusService.get(statusId),
				parentCapabilityId, capabilityName, capabilityDescription, PaceOfChange.valueOf(paceOfChange),
				TargetOperatingModel.valueOf(targetOperatingModel), resourceQuality, informationQuality,
				applicationFit));
	}

	@Override
	public Capability save(Capability capability) {
		updateLevel(capability);
		Capability savedCapability = capabilityDAL.save(capability);
		environmentService.addCapability(capability.getEnvironment().getEnvironmentId(), savedCapability);
		return savedCapability;
	}

	@Override
	public Capability get(@NotNull @Min(1) Integer id) throws NoSuchElementException {
		return capabilityDAL.findById(id).get();
	}

	@Override
	public List<Capability> getAll() {
		return capabilityDAL.findAll();
	}

	@Override
	public Capability update(@NotNull @Min(1) Integer capabilityId, @NotNull @Min(1) Integer environmentId,
			@NotNull @Min(1) Integer statusId, Integer parentCapabilityId, @NotBlank String capabilityName,
			String capabilityDescription, String paceOfChange, String targetOperatingModel, Integer resourceQuality,
			Integer informationQuality, Integer applicationFit) {
		// TODO duplicate name in same environment check
		return save(new Capability(capabilityId, environmentService.get(environmentId), statusService.get(statusId),
				parentCapabilityId, capabilityName, capabilityDescription, PaceOfChange.valueOf(paceOfChange),
				TargetOperatingModel.valueOf(targetOperatingModel), resourceQuality, informationQuality,
				applicationFit));
	}

	// TODO try catch for out of bounds exception
	@Override
	public void updateLevel(Capability capability) {
		if (capability.getParentCapabilityId() == 0)
			capability.setLevel(CapabilityLevel.ONE);
		else
			capability.setLevel(capabilityDAL.getOne(capability.getParentCapabilityId()).getLevel().next());
	}

	// TODO write unit tests!
	@Override
	public void delete(Integer id) {
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
		Environment environment = environmentService.get(environmentId);
		List<Capability> capabilities = capabilityDAL.findByEnvironment(environment);
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilitiesByLevel(String level) {
		if (Arrays.stream(CapabilityLevel.values()).noneMatch((capLevel) -> capLevel.name().equals(level))) {
			throw new EnumException("CapabilityLevel is not valid.");
		}

		CapabilityLevel capabilityLevel = CapabilityLevel.valueOf(level);
		List<Capability> capabilities = capabilityDAL.findByLevel(capabilityLevel);
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilityChildren(Integer parentId) {
		List<Capability> capabilities = capabilityDAL.findByParentCapabilityId(parentId);
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilitiesByParentIdAndLevel(Integer parentId, String level) {
		if (Arrays.stream(CapabilityLevel.values()).noneMatch((capLevel) -> capLevel.name().equals(level))) {
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
		return;
	}

	@Override
	public Set<Project> getAllProjectsByCapabilityId(Integer capabilityId) {
		Capability capability = get(capabilityId);
		return capability.getProjects();
	}

	@Override
	public void addBusinessProcess(Integer capabilityId, Integer businessProcessId) {
		Capability capability = get(capabilityId);
		BusinessProcess businessProcess = businessProcessService.get(businessProcessId);
		capability.addBusinessProcess(businessProcess);
		return;
	}

	@Override
	public void deleteBusinessProcess(Integer capabilityId, Integer businessProcessId) {
		Capability capability = get(capabilityId);
		BusinessProcess businessProcess = businessProcessService.get(businessProcessId);
		capability.removeBusinessProcess(businessProcess);
		return;
	}

	@Override
	public Set<BusinessProcess> getAllBusinessProcessByCapabilityId(Integer capabilityId) {
		Capability capability = get(capabilityId);
		return capability.getBusinessProcess();
	}

	@Override
	public void addResource(Integer capabilityId, Integer resourceId) {
		Capability capability = get(capabilityId);
		Resource resource = resourceService.get(resourceId);
		capability.addResource(resource);
		return;
	}

	@Override
	public void deleteResource(Integer capabilityId, Integer resourceId) {
		Capability capability = get(capabilityId);
		Resource resource = resourceService.get(resourceId);
		capability.removeResource(resource);
		return;
	}

	@Override
	public Set<Resource> getAllResourceByResourceId(Integer capabilityId) {
		Capability capability = get(capabilityId);
		return capability.getResources();
	}

}
