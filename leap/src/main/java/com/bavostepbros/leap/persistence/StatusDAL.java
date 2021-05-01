package com.bavostepbros.leap.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Status;

public interface StatusDAL extends JpaRepository<Status, Integer> {

}
