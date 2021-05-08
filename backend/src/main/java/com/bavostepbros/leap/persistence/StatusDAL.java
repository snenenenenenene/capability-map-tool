package com.bavostepbros.leap.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Status;

/**
*
* @author Bavo Van Meel
*
*/
public interface StatusDAL extends JpaRepository<Status, Integer> {
	List<Status> findByValidityPeriod(LocalDate validityPeriod);
}
