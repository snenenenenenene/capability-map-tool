package com.bavostepbros.leap.database;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.model.Capability;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CapabilityServiceImpl implements CapabilityService {

	private final CapabilityDAL capabilityDAL;

	@Autowired
	public CapabilityServiceImpl(CapabilityDAL capabilityDAL) {
		super();
		this.capabilityDAL = capabilityDAL;
	}

	@Override
	public boolean save(Capability capability) {
		List<Capability> capabilities = getAll();
		List<Capability> results = capabilities.stream()
				.filter(cap -> capability.getCapabilityName().equals(cap.getCapabilityName()))
				.collect(Collectors.toList());
		if (results.size() > 0) {
			// TODO add number to duplicate.
			return false;
		} else {
			capabilityDAL.save(capability);
			return true;
		}
	}

	@Override
	public Capability get(Integer id) {
		Capability capability = capabilityDAL.findById(id).get();
		return capability;
	}
	
	@Override
	public List<Capability> getAll() {
		List<Capability> capabilities = capabilityDAL.findAll();
		return capabilities;
	}
	
	@Override
	public void update(Capability capability) {
		capabilityDAL.save(capability);
	}

	@Override
	public void delete(Integer id) {
		capabilityDAL.deleteById(id);
	}
	
}
