package com.bavostepbros.leap.database;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.model.Capability;
import com.bavostepbros.leap.model.Environment;

@Service
@Transactional
public class CapabilityServiceImpl implements CapabilityService {

	private final CapabilityDAL capabilityDAL;
	private final EnvironmentDAL environmentDAL;

	@Autowired
	public CapabilityServiceImpl(CapabilityDAL capabilityDAL, EnvironmentDAL environmentDAL) {
		super();
		this.capabilityDAL = capabilityDAL;
		this.environmentDAL = environmentDAL;
	}

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
		return capabilityDAL.findByEnvironment(environment);
	}
	
}
