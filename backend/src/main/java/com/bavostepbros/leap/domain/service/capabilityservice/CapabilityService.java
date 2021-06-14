package com.bavostepbros.leap.domain.service.capabilityservice;

import java.util.List;
import java.util.Set;

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Resource;

/**
 *
 * @author Bavo Van Meel
 *
 */
public interface CapabilityService {
	Capability save(Integer environmentId, Integer statusId, Integer parentCapabilityId, String capabilityName,
			String capabilityDescription, String paceOfChange, String targetOperatingModel, Integer resourceQuality,
			Integer informationQuality, Integer applicationFit);

	Capability save (Capability capability);

	Capability get(Integer id);

	List<Capability> getAll();

	Capability update(Integer capabilityId, Integer environmentId, Integer statusId, Integer parentCapabilityId,
			String capabilityName, String capabilityDescription, String paceOfChange, String targetOperatingModel,
			Integer resourceQuality, Integer informationQuality, Integer applicationFit);

	void updateLevel(Capability capability);

	void delete(Integer id);

	boolean existsById(Integer id);

	boolean existsByCapabilityName(String capabilityName);

	Capability getCapabilityByCapabilityName(String capabilityName);

	List<Capability> getCapabilitiesByEnvironment(Integer environmentId);

	List<Capability> getCapabilitiesByLevel(String level);

	List<Capability> getCapabilityChildren(Integer parentId);

	List<Capability> getCapabilitiesByParentIdAndLevel(Integer parentId, String level);

	void addProject(Integer capabilityId, Integer projectId);

	void deleteProject(Integer capabilityId, Integer projectId);

	List<Project> getAllProjectsByCapabilityId(Integer capabilityId);

	void addBusinessProcess(Integer capabilityId, Integer businessProcessId);

	void deleteBusinessProcess(Integer capabilityId, Integer businessProcessId);

	Set<BusinessProcess> getAllBusinessProcessByCapabilityId(Integer capabilityId);

	void addResource(Integer capabilityId, Integer resourceId);

	void deleteResource(Integer capabilityId, Integer resourceId);

	List<Resource> getAllResourceByResourceId(Integer capabilityId);
}
