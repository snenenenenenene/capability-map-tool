package com.bavostepbros.leap.domain.service.technologyservice;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.persistence.TechnologyDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TechnologyServiceImpl implements TechnologyService {
	
	@Autowired
	private TechnologyDAL technologyDAL;
	
	@Override
	public Technology save(String technologyName) {	
		Technology technology = new Technology(technologyName);
		return technologyDAL.save(technology);
	}

	@Override
	public Technology get(Integer technologyId) {
		Optional<Technology> technology = technologyDAL.findById(technologyId);
		technology.orElseThrow(() -> new NullPointerException("Technology does not exist."));
		return technology.get();
	}

	@Override
	public Technology update(Integer technologyId, String technologyName) {
		Technology technology = new Technology(technologyId, technologyName);
		return technologyDAL.save(technology);
	}

	@Override
	public void delete(Integer technologyId) {
		technologyDAL.deleteById(technologyId);
	}
	
	@Override
	public Technology getByTechnologyName(String technologyName) {
		Optional<Technology> technology = technologyDAL.findByTechnologyName(technologyName);
		technology.orElseThrow(() -> new NullPointerException("Technology does not exist."));
		return technology.get();
	}

	@Override
	public List<Technology> getAll() {
		return technologyDAL.findAll();
	}

	@Override
	public boolean existsById(Integer technologyId) {
		return technologyDAL.existsById(technologyId);
	}

	@Override
	public boolean existsByTechnologyName(String technologyName) {
		return !technologyDAL.findByTechnologyName(technologyName).isEmpty();
	}

}
