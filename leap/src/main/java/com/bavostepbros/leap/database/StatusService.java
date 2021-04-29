package com.bavostepbros.leap.database;

import com.bavostepbros.leap.model.Status;

public interface StatusService {
	void save(Status status);
	Status get(Integer id);
	void update(Status status);
	void delete(Status status);
}
