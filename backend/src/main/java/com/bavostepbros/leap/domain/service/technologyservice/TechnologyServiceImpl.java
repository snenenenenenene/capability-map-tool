package com.bavostepbros.leap.domain.service.technologyservice;

import java.util.List;

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
		Technology technology = technologyDAL.findById(technologyId).get();
		return technology;
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
	public List<Technology> getAll() {
		return technologyDAL.findAll();
	}

}
