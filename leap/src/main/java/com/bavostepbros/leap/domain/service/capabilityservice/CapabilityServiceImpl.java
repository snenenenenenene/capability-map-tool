package com.bavostepbros.leap.domain.service.capabilityservice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
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

	@Override
	public boolean save(Capability capability) {
		List<Capability> capabilities = getAll();
		List<Capability> results = capabilities.stream()
				.filter(cap -> capability.getCapabilityName().equals(cap.getCapabilityName()))
				.collect(Collectors.toList());
		if (results.size() > 0) {
			return false;
		} else {
			capabilityDAL.save(capability);
			return true;
		}
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
	public void update(Capability capability) {
		try {
			capabilityDAL.save(capability);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		boolean result = capabilityDAL.existsById(id);
		return result;
	}

	@Override
	public boolean existsByCapabilityName(String capabilityName) {
		boolean result = capabilityDAL.findByCapabilityName(capabilityName).isEmpty();
		return result;
	}

}
