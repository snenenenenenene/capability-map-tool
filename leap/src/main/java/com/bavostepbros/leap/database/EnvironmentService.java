package com.bavostepbros.leap.database;

import com.bavostepbros.leap.model.Environment;

public interface EnvironmentService {
	void save(Environment environment);
	Environment get(Integer id);
	void update(Environment environment);
	void delete(Environment environment);
}
