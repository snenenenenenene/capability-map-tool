package com.bavostepbros.leap.domain.service.capabilityservice;

import java.time.LocalDate;
import java.util.List;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;

public interface CapabilityService {
	Capability save(Integer environmentId, String environmentName, Integer statusId, LocalDate validityPeriod,
			Integer parentCapabilityId, String capabilityName, CapabilityLevel level, boolean paceOfChange,
			String targetOperatingModel, Integer resourceQuality, Integer informationQuality, Integer applicationFit);

	Capability get(Integer id);

	List<Capability> getAll();

	Capability update(Integer capabilityId, Integer environmentId, String environmentName, Integer statusId,
			LocalDate validityPeriod, Integer parentCapabilityId, String capabilityName, CapabilityLevel level,
			boolean paceOfChange, String targetOperatingModel, Integer resourceQuality, Integer informationQuality,
			Integer applicationFit);

	void delete(Integer id);

	boolean existsById(Integer id);

	boolean existsByCapabilityName(String capabilityName);

	List<Capability> getCapabilitiesByEnvironment(Integer environmentId);

	List<Capability> getCapabilitiesByLevel(CapabilityLevel level);

	List<Capability> getCapabilityChildren(Integer parentId);

	List<Capability> getCapabilitiesByParentIdAndLevel(Integer parentId, CapabilityLevel level);
}
