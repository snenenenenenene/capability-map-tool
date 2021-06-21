package com.bavostepbros.leap.domain.service.statusservice;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.bavostepbros.leap.domain.model.Status;

public interface StatusService {
	Status save(LocalDate validityPeriod);
	Status get(Integer id);
	Status getByValidityPeriod(@NotNull LocalDate validityPeriod);
	List<Status> getAll();
	Status update(Integer statusId, @NotNull LocalDate validityPeriod);
	void delete(Integer id);
	boolean existsById(Integer id);
	boolean existsByValidityPeriod(@NotNull LocalDate validityPeriod);
}
