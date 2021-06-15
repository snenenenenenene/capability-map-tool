package com.bavostepbros.leap.domain.service.capabilityservice;

import java.util.*;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bavostepbros.leap.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.customexceptions.CapabilityException;
import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.EnumException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
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

	
	/** 
	 * @param environmentId
	 * @param statusId
	 * @param parentCapabilityId
	 * @param capabilityName
	 * @param capabilityDescription
	 * @param paceOfChange
	 * @param targetOperatingModel
	 * @param resourceQuality
	 * @param informationQuality
	 * @param applicationFit
	 * @return Capability
	 */
	@Override
	public Capability save(
			@NotNull @Min(1) Integer environmentId,
			@NotNull @Min(1) Integer statusId,
			Integer parentCapabilityId,
			@NotBlank String capabilityName,
			String capabilityDescription,
			String paceOfChange,
			String targetOperatingModel,
			Integer resourceQuality,
			Integer informationQuality,
			Integer applicationFit) {
		return save(new Capability(
				environmentService.get(environmentId),
				statusService.get(statusId),
				parentCapabilityId,
				capabilityName,
				capabilityDescription,
				PaceOfChange.valueOf(paceOfChange),
				TargetOperatingModel.valueOf(targetOperatingModel),
				resourceQuality,
				informationQuality,
				applicationFit
		));
	}

	@Override
	public Capability save (Capability capability) {
		updateLevel(capability);
		Capability savedCapability = capabilityDAL.save(capability);
		environmentService.addCapability(capability.getEnvironment().getEnvironmentId(), savedCapability);
		return savedCapability;
	}

	
	/** 
	 * @param id
	 * @return Capability
	 */
	@Override
	public Capability get(@NotNull @Min(1) Integer id) throws NoSuchElementException {
		return capabilityDAL.findById(id).get();
	}

	
	/** 
	 * @return List<Capability>
	 */
	@Override
	public List<Capability> getAll() {
		return capabilityDAL.findAll();
	}

	
	/** 
	 * @param capabilityId
	 * @param environmentId
	 * @param statusId
	 * @param parentCapabilityId
	 * @param capabilityName
	 * @param capabilityDescription
	 * @param paceOfChange
	 * @param targetOperatingModel
	 * @param resourceQuality
	 * @param informationQuality
	 * @param applicationFit
	 * @return Capability
	 */
	@Override
	public Capability update(
			@NotNull @Min(1) Integer capabilityId,
			@NotNull @Min(1) Integer environmentId,
			@NotNull @Min(1) Integer statusId,
			Integer parentCapabilityId,
			@NotBlank String capabilityName,
			String capabilityDescription,
			String paceOfChange,
			String targetOperatingModel,
			Integer resourceQuality,
			Integer informationQuality,
			Integer applicationFit) {
		// TODO duplicate name in same environment check
			return update(capabilityId, new Capability(
					capabilityId,
					environmentService.get(environmentId),
					statusService.get(statusId),
					parentCapabilityId,
					capabilityName,
					capabilityDescription,
					PaceOfChange.valueOf(paceOfChange),
					TargetOperatingModel.valueOf(targetOperatingModel),
					resourceQuality,
					informationQuality,
					applicationFit
			));
	}

	@Override
	public Capability update(@NotNull @Min(1) Integer capabilityId, Capability capability) {
		updateLevel(capability);
		return capabilityDAL.save(capability);
	}

	
	/** 
	 * @param capability
	 */
	// TODO try catch for out of bounds exception
	@Override
	public void updateLevel(Capability capability) {
		if (capability.getParentCapabilityId() == 0)
			capability.setLevel(CapabilityLevel.ONE);
		else
			capability.setLevel(capabilityDAL.getOne(capability.getParentCapabilityId()).getLevel().next());
	}

	
	/** 
	 * @param id
	 */
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

	
	/** 
	 * @param environmentId
	 * @return List<Capability>
	 */
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

	
	/** 
	 * @param level
	 * @return List<Capability>
	 */
	@Override
	public List<Capability> getCapabilitiesByLevel(String level) {
		if (level == null) {
			throw new InvalidInputException("CapabilityLevel is not valid.");
		}
		if (Arrays.stream(CapabilityLevel.values()).noneMatch((capLevel) -> capLevel.name().equals(level))) {
			throw new EnumException("CapabilityLevel is not valid.");
		}

		CapabilityLevel capabilityLevel = CapabilityLevel.valueOf(level);
		List<Capability> capabilities = capabilityDAL.findByLevel(capabilityLevel);
		return capabilities;
	}

	
	/** 
	 * @param parentId
	 * @return List<Capability>
	 */
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

	
	/** 
	 * @param parentId
	 * @param level
	 * @return List<Capability>
	 */
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
		if (Arrays.stream(CapabilityLevel.values()).noneMatch((capLevel) -> capLevel.name().equals(level))) {
			throw new EnumException("CapabilityLevel is not valid.");
		}

		CapabilityLevel capabilityLevel = CapabilityLevel.valueOf(level);
		List<Capability> capabilities = capabilityDAL.findByParentCapabilityIdAndLevel(parentId, capabilityLevel);
		return capabilities;
	}

	
	/** 
	 * @param id
	 * @return boolean
	 */
	@Override
	public boolean existsById(Integer id) {
		return capabilityDAL.existsById(id);
	}

	
	/** 
	 * @param capabilityName
	 * @return boolean
	 */
	@Override
	public boolean existsByCapabilityName(String capabilityName) {
		return !capabilityDAL.findByCapabilityName(capabilityName).isEmpty();
	}

	
	/** 
	 * @param capabilityName
	 * @return Capability
	 */
	// TODO write unit tests for this one!
	@Override
	public Capability getCapabilityByCapabilityName(String capabilityName) {
		if (capabilityName == null || capabilityName.isBlank() || capabilityName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}

		return capabilityDAL.findByCapabilityName(capabilityName).get();
	}

	
	/** 
	 * @param capabilityId
	 * @param projectId
	 */
	@Override
	public void addProject(Integer capabilityId, Integer projectId) {
		Capability capability = get(capabilityId);
		Project project = projectService.get(projectId);
		capability.addProject(project);
		return;
	}

	
	/** 
	 * @param capabilityId
	 * @param projectId
	 */
	@Override
	public void deleteProject(Integer capabilityId, Integer projectId) {
		Capability capability = get(capabilityId);
		Project project = projectService.get(projectId);
		capability.removeProject(project);
		return;
	}

	
	/** 
	 * @param capabilityId
	 * @return List<Project>
	 */
	@Override
	public List<Project> getAllProjectsByCapabilityId(Integer capabilityId) {
		Capability capability = get(capabilityId);
		return capability.getProjects();
	}

	
	/** 
	 * @param capabilityId
	 * @param businessProcessId
	 */
	@Override
	public void addBusinessProcess(Integer capabilityId, Integer businessProcessId) {
		Capability capability = get(capabilityId);
		BusinessProcess businessProcess = businessProcessService.get(businessProcessId);
		capability.addBusinessProcess(businessProcess);
		return;
	}

	
	/** 
	 * @param capabilityId
	 * @param businessProcessId
	 */
	@Override
	public void deleteBusinessProcess(Integer capabilityId, Integer businessProcessId) {
		Capability capability = get(capabilityId);
		BusinessProcess businessProcess = businessProcessService.get(businessProcessId);
		capability.removeBusinessProcess(businessProcess);
		return;
	}

	
	/** 
	 * @param capabilityId
	 * @return Set<BusinessProcess>
	 */
	@Override
	public Set<BusinessProcess> getAllBusinessProcessByCapabilityId(Integer capabilityId) {
		Capability capability = get(capabilityId);
		return capability.getBusinessProcess();
	}

	
	/** 
	 * @param capabilityId
	 * @param resourceId
	 */
	@Override
	public void addResource(Integer capabilityId, Integer resourceId) {
		Capability capability = get(capabilityId);
		Resource resource = resourceService.get(resourceId);
		capability.addResource(resource);
		return;
	}

	
	/** 
	 * @param capabilityId
	 * @param resourceId
	 */
	@Override
	public void deleteResource(Integer capabilityId, Integer resourceId) {
		Capability capability = get(capabilityId);
		Resource resource = resourceService.get(resourceId);
		capability.removeResource(resource);
		return;
	}

	
	/** 
	 * @param capabilityId
	 * @return List<Resource>
	 */
	@Override
	public List<Resource> getAllResourceByResourceId(Integer capabilityId) {
		Capability capability = get(capabilityId);
		return capability.getResources();
	}

}
