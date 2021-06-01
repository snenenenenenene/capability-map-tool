package com.bavostepbros.leap.domain.service.businessprocessservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.BusinessProcess;

public interface BusinessProcessService {
	BusinessProcess save(String businessProcessName, String businessProcessDescription);
	BusinessProcess get(Integer businessProcessId);
	BusinessProcess update(Integer businessProcessId, String businessProcessName, String businessProcessDescription);
	void delete(Integer businessProcessId);
	BusinessProcess getBusinessProcessByName(String businessProcessName);
	List<BusinessProcess> getAll();
}
