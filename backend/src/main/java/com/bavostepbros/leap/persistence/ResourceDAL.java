package com.bavostepbros.leap.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Resource;

public interface ResourceDAL extends JpaRepository<Resource, Integer> {
	Optional<Resource> findByResourceName(String resourceName);
}
