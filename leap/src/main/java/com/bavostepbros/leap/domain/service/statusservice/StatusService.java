package com.bavostepbros.leap.domain.service.statusservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Status;

public interface StatusService {
	boolean save(Status status);
	Status get(Integer id);
	List<Status> getAll();
	void update(Status status);
	void delete(Integer id);
}
