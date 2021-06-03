package com.bavostepbros.leap.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Information;

public interface InformationDAL extends JpaRepository<Information, Integer> {
	Optional<Information> findByInformationName(String informationName);
}
