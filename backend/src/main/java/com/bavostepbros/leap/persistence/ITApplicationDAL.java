package com.bavostepbros.leap.persistence;

import com.bavostepbros.leap.domain.model.ITApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITApplicationDAL extends JpaRepository<ITApplication, Long> {
    List<ITApplication> findByName(String name);
}
