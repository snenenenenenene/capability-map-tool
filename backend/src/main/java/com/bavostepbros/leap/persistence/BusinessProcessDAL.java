package com.bavostepbros.leap.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.BusinessProcess;

public interface BusinessProcessDAL extends JpaRepository<BusinessProcess, Integer> {
	Optional<BusinessProcess> findByBusinessProcessName(String businessProcessName);
	
}
