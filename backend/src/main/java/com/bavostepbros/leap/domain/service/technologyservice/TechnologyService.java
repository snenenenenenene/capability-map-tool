package com.bavostepbros.leap.domain.service.technologyservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Technology;

public interface TechnologyService {
	Technology save(String technologyName);

	Technology get(Integer technologyId);

	Technology update(Integer technologyId, String technologyName);

	void delete(Integer technologyId);

	Technology getByTechnologyName(String technologyName);

	List<Technology> getAll();

	boolean existsById(Integer technologyId);

	boolean existsByTechnologyName(String technologyName);
}
