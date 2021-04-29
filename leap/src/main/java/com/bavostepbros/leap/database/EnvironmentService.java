package com.bavostepbros.leap.database;

import com.bavostepbros.leap.model.Environment;

public interface EnvironmentService {
	boolean save(Environment environment);
	Environment get(Integer id);
	void update(Environment environment);
	void delete(Integer id);
}
