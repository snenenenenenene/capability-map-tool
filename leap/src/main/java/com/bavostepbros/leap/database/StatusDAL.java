package com.bavostepbros.leap.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.model.Status;

public interface StatusDAL extends JpaRepository<Status, Integer> {

}
