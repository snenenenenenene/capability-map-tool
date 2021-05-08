package com.bavostepbros.leap.domain.service.capabilityservice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.persistence.CapabilityDAL;
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
public class CapabilityServiceImpl implements CapabilityService {

	@Autowired
	private CapabilityDAL capabilityDAL;

	@Autowired
	private EnvironmentDAL environmentDAL;

	@Override
	public Capability save(Integer environmentId, String environmentName, Integer statusId, LocalDate validityPeriod,
			Integer parentCapabilityId, String capabilityName, CapabilityLevel level, boolean paceOfChange,
			String targetOperatingModel, Integer resourceQuality, Integer informationQuality, Integer applicationFit) {
		Environment environment = new Environment(environmentId, environmentName);
		Status status = new Status(statusId, validityPeriod);
		Capability capability = new Capability(environment, status, parentCapabilityId, capabilityName, level,
				paceOfChange, targetOperatingModel, resourceQuality, informationQuality, applicationFit);
		Capability savedCapability = capabilityDAL.save(capability);
		return savedCapability;
	}

	@Override
	public Capability get(Integer id) {
		Capability capability = null;
		try {
			capability = capabilityDAL.findById(id).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return capability;
	}

	@Override
	public List<Capability> getAll() {
		List<Capability> capabilities = new ArrayList<Capability>();
		try {
			capabilities = capabilityDAL.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return capabilities;
	}

	@Override
	public Capability update(Integer capabilityId, Integer environmentId, String environmentName, Integer statusId,
			LocalDate validityPeriod, Integer parentCapabilityId, String capabilityName, CapabilityLevel level,
			boolean paceOfChange, String targetOperatingModel, Integer resourceQuality, Integer informationQuality,
			Integer applicationFit) {
		Environment environment = new Environment(environmentId, environmentName);
		Status status = new Status(statusId, validityPeriod);
		Capability capability = new Capability(capabilityId, environment, status, parentCapabilityId, capabilityName,
				level, paceOfChange, targetOperatingModel, resourceQuality, informationQuality, applicationFit);
		Capability updatedCapability = capabilityDAL.save(capability);
		return updatedCapability;
	}

	@Override
	public void delete(Integer id) {
		try {
			capabilityDAL.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Capability> getCapabilitiesByEnvironment(Integer environmentId) {
		Environment environment = environmentDAL.findById(environmentId).get();
		List<Capability> capabilities = capabilityDAL.findByEnvironment(environment);
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilitiesByLevel(CapabilityLevel level) {
		List<Capability> capabilities = capabilityDAL.findByLevel(level);
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilityChildren(Integer parentId) {
		List<Capability> capabilities = capabilityDAL.findByParentCapabilityId(parentId);
		return capabilities;
	}

	@Override
	public List<Capability> getCapabilitiesByParentIdAndLevel(Integer parentId, CapabilityLevel level) {
		List<Capability> capabilities = capabilityDAL.findByParentCapabilityIdAndLevel(parentId, level);
		capabilities.forEach(c -> System.out.println(c));
		return capabilities;
	}

	@Override
	public boolean existsById(Integer id) {
		return capabilityDAL.existsById(id);
	}

	@Override
	public boolean existsByCapabilityName(String capabilityName) {
		return capabilityDAL.findByCapabilityName(capabilityName).isEmpty();
	}

}
