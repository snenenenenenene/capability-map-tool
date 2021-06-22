package com.bavostepbros.leap.domain.service.capabilityservice;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.customexceptions.EnumException;
import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityApplication;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Resource;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.businessprocessservice.BusinessProcessService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.projectservice.ProjectService;
import com.bavostepbros.leap.domain.service.resourceservice.ResourceService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.CapabilityApplicationDAL;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CapabilityServiceImpl implements CapabilityService {

	@Autowired
	private CapabilityDAL capabilityDAL;
	
	@Autowired
	private EnvironmentDAL environmentDAL;

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
	
	@Autowired
	private CapabilityApplicationDAL capabilityApplicationDAL;

	@Override
	public Capability save( Integer environmentId, Integer statusId,
			Integer parentCapabilityId, String capabilityName, String capabilityDescription,
			String paceOfChange, String targetOperatingModel, Integer resourceQuality, Double informationQuality,
			Double applicationFit) {
		
		Capability capability = save(new Capability(environmentService.get(environmentId), statusService.get(statusId),
				parentCapabilityId, capabilityName, capabilityDescription, PaceOfChange.valueOf(paceOfChange),
				TargetOperatingModel.valueOf(targetOperatingModel), resourceQuality, informationQuality,
				applicationFit));
		calculateValues(capability.getCapabilityId());
		return capability;
	}

	@Override
	public Capability save(Capability capability) {
		updateLevel(capability);
		Capability savedCapability = capabilityDAL.save(capability);
		environmentService.addCapability(capability.getEnvironment().getEnvironmentId(), savedCapability);
		return savedCapability;
	}

	@Override
	public Capability get(Integer id) {
		Optional<Capability> capability = capabilityDAL.findById(id);
		capability.orElseThrow(() -> new NullPointerException("Capability does not exist."));
		Capability cap = capability.get();
		calculateValues(cap.getCapabilityId());
		return cap;
	}

	@Override
	public List<Capability> getAll() {
		List<Capability> capabilities = capabilityDAL.findAll();
		for (Capability capability : capabilities) {
			calculateValues(capability.getCapabilityId());
		}
		return capabilities;
	}

	@Override
	public Capability update(Integer capabilityId, Integer environmentId,
			Integer statusId, Integer parentCapabilityId, String capabilityName,
			String capabilityDescription, String paceOfChange, String targetOperatingModel, Integer resourceQuality,
			Double informationQuality, Double applicationFit) {
		Capability capability = new Capability(capabilityId, environmentService.get(environmentId), statusService.get(statusId),
				parentCapabilityId, capabilityName, capabilityDescription, PaceOfChange.valueOf(paceOfChange),
				TargetOperatingModel.valueOf(targetOperatingModel), resourceQuality, informationQuality,
				applicationFit);
		calculateValues(capability.getCapabilityId());
		return capability;
	}

	@Override
	public void updateLevel(Capability capability) {
		if (capability.getParentCapabilityId() == 0)
			capability.setLevel(CapabilityLevel.ONE);
		else
			capability.setLevel(capabilityDAL.getOne(capability.getParentCapabilityId()).getLevel().next());
	}

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
		Optional<Environment> environment = environmentDAL.findById(environmentId);
		environment.orElseThrow(() -> new NullPointerException("Environment does not exist."));
		List<Capability> capabilities = capabilityDAL.findByEnvironment(environment.get());
		for (Capability capability : capabilities) {
			calculateValues(capability.getCapabilityId());
		}
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilitiesByLevel(String level) {
		if (Arrays.stream(CapabilityLevel.values()).noneMatch((capLevel) -> capLevel.name().equals(level))) {
			throw new EnumException("CapabilityLevel is not valid.");
		}

		CapabilityLevel capabilityLevel = CapabilityLevel.valueOf(level);
		List<Capability> capabilities = capabilityDAL.findByLevel(capabilityLevel);
		for (Capability capability : capabilities) {
			calculateValues(capability.getCapabilityId());
		}
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilityChildren(Integer parentId) {
		List<Capability> capabilities = capabilityDAL.findByParentCapabilityId(parentId);
		for (Capability capability : capabilities) {
			calculateValues(capability.getCapabilityId());
		}
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilitiesByParentIdAndLevel(Integer parentId, String level) {
		if (Arrays.stream(CapabilityLevel.values()).noneMatch((capLevel) -> capLevel.name().equals(level))) {
			throw new EnumException("CapabilityLevel is not valid.");
		}

		CapabilityLevel capabilityLevel = CapabilityLevel.valueOf(level);
		List<Capability> capabilities = capabilityDAL.findByParentCapabilityIdAndLevel(parentId, capabilityLevel);
		for (Capability capability : capabilities) {
			calculateValues(capability.getCapabilityId());
		}
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

	@Override
	public Capability getCapabilityByCapabilityName(String capabilityName) {
		Optional<Capability> capability = capabilityDAL.findByCapabilityName(capabilityName);
		capability.orElseThrow(() -> new NullPointerException("Capability does not exist."));
		Capability cap = capability.get();
		calculateValues(cap.getCapabilityId());
		return cap;
	}

	@Override
	public Capability addProject(Integer capabilityId, Integer projectId) {
		Capability capability = get(capabilityId);
		Project project = projectService.get(projectId);
		capability.addProject(project);
		return capabilityDAL.save(capability);
	}

	@Override
	public void deleteProject(Integer capabilityId, Integer projectId) {
		Capability capability = get(capabilityId);
		Project project = projectService.get(projectId);
		capability.removeProject(project);
		capabilityDAL.save(capability);
		return;
	}

	@Override
	public Set<Project> getAllProjectsByCapabilityId(Integer capabilityId) {
		Capability capability = get(capabilityId);
		return capability.getProjects();
	}

	@Override
	public Capability addBusinessProcess(Integer capabilityId, Integer businessProcessId) {
		Capability capability = get(capabilityId);
		BusinessProcess businessProcess = businessProcessService.get(businessProcessId);
		capability.addBusinessProcess(businessProcess);
		return capabilityDAL.save(capability);
	}

	@Override
	public void deleteBusinessProcess(Integer capabilityId, Integer businessProcessId) {
		Capability capability = get(capabilityId);
		BusinessProcess businessProcess = businessProcessService.get(businessProcessId);
		capability.removeBusinessProcess(businessProcess);
		capabilityDAL.save(capability);
		return;
	}

	@Override
	public Set<BusinessProcess> getAllBusinessProcessByCapabilityId(Integer capabilityId) {
		Capability capability = get(capabilityId);
		return capability.getBusinessProcess();
	}

	@Override
	public Capability addResource(Integer capabilityId, Integer resourceId) {
		Capability capability = get(capabilityId);
		Resource resource = resourceService.get(resourceId);
		capability.addResource(resource);
		return capabilityDAL.save(capability);
	}

	@Override
	public void deleteResource(Integer capabilityId, Integer resourceId) {
		Capability capability = get(capabilityId);
		Resource resource = resourceService.get(resourceId);
		capability.removeResource(resource);
		capabilityDAL.save(capability);
		return;
	}

	@Override
	public Set<Resource> getAllResourceByResourceId(Integer capabilityId) {
		Capability capability = get(capabilityId);
		return capability.getResources();
	}
	
	public void calculateValues(Integer capabilityId) {
		Optional<Capability> cap = capabilityDAL.findById(capabilityId);
		cap.orElseThrow(() -> new NullPointerException("Capability does not exist"));
		Capability capability = cap.get();
		List<CapabilityApplication> capabilityApplications = capabilityApplicationDAL.findByCapability(capability);
		Double avgInf = 0.0;
		Double avgApp = 0.0;
		
		for (CapabilityApplication capApp : capabilityApplications) {
			avgInf += (capApp.getCompleteness() + capApp.getCorrectnessInformationFit() +
					capApp.getAvailability()) / 3 * capApp.getImportance();
		}
		
		for (CapabilityApplication capApp : capabilityApplications) {
			avgApp += (capApp.getEfficiencySupport() + capApp.getFunctionalCoverage() +
					capApp.getCorrectnessBusinessFit() + capApp.getFuturePotential()) 
					/ 3 * capApp.getImportance();
		}
		
		capability.setInformationQuality(avgInf);
		capability.setApplicationFit(avgApp);
		capabilityDAL.save(capability);
	}

}
