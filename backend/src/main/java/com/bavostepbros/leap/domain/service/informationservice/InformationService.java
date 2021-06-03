package com.bavostepbros.leap.domain.service.informationservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Information;

public interface InformationService {
	Information save(String informationName, String informationDescription);
	Information get(Integer informationId);
	Information update(Integer informationId, String informationName, String informationDescription);
	void delete(Integer informationId);
	Information getInformationByName(String informationName);
	List<Information> getAll();
}
