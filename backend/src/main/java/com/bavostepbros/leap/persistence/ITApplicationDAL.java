package com.bavostepbros.leap.persistence;

import com.bavostepbros.leap.domain.model.ITApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITApplicationDAL extends JpaRepository<ITApplication, Integer> {
    Optional<ITApplication> findByName(String name);
}
