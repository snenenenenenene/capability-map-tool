package com.bavostepbros.leap.domain.service.capabilityservice;

import java.util.List;
import java.util.Set;

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Resource;

public interface CapabilityService {
	Capability save(Integer environmentId, Integer statusId, Integer parentCapabilityId, String capabilityName,
			String capabilityDescription, String paceOfChange, String targetOperatingModel, Integer resourceQuality);

	Capability save (Capability capability);

	Capability get(Integer id);

	List<Capability> getAll();

	Capability update(Integer capabilityId, Integer environmentId, Integer statusId, Integer parentCapabilityId,
			String capabilityName, String capabilityDescription, String paceOfChange, String targetOperatingModel,
			Integer resourceQuality);

	void updateLevel(Capability capability);

	void delete(Integer id);

	boolean existsById(Integer id);

	boolean existsByCapabilityName(String capabilityName);

	Capability getCapabilityByCapabilityName(String capabilityName);

	List<Capability> getCapabilitiesByEnvironment(Integer environmentId);

	List<Capability> getCapabilitiesByLevel(String level);

	List<Capability> getCapabilityChildren(Integer parentId);

	List<Capability> getCapabilitiesByParentIdAndLevel(Integer parentId, String level);

	Capability addProject(Integer capabilityId, Integer projectId);

	void deleteProject(Integer capabilityId, Integer projectId);

	Set<Project> getAllProjectsByCapabilityId(Integer capabilityId);

	Capability addBusinessProcess(Integer capabilityId, Integer businessProcessId);

	void deleteBusinessProcess(Integer capabilityId, Integer businessProcessId);

	Set<BusinessProcess> getAllBusinessProcessByCapabilityId(Integer capabilityId);

	Capability addResource(Integer capabilityId, Integer resourceId);

	void deleteResource(Integer capabilityId, Integer resourceId);

	Set<Resource> getAllResourceByResourceId(Integer capabilityId);
}
