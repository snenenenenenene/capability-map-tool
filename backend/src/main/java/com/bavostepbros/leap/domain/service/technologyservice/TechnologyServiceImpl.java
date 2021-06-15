package com.bavostepbros.leap.domain.service.technologyservice;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.TechnologyException;
import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.persistence.TechnologyDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TechnologyServiceImpl implements TechnologyService {
	
	@Autowired
	private TechnologyDAL technologyDAL;
	
	
	/** 
	 * @param technologyName
	 * @return Technology
	 */
	@Override
	public Technology save(String technologyName) {	
		if (technologyName == null || technologyName.isBlank() || technologyName.isEmpty()) {
			throw new InvalidInputException("Technology name is not valid.");
		}
		if (existsByTechnologyName(technologyName)) {
			throw new DuplicateValueException("Technology name already exists.");
		}
		
		Technology technology = new Technology(technologyName);
		return technologyDAL.save(technology);
	}

	
	/** 
	 * @param technologyId
	 * @return Technology
	 */
	@Override
	public Technology get(Integer technologyId) {
		if (technologyId == null || technologyId.equals(0)) {
			throw new InvalidInputException("Technology ID is not valid.");
		}
		if (!existsById(technologyId)) {
			throw new IndexDoesNotExistException("Technology ID does not exists.");
		}
		
		Technology technology = technologyDAL.findById(technologyId).get();
		return technology;
	}

	
	/** 
	 * @param technologyId
	 * @param technologyName
	 * @return Technology
	 */
	@Override
	public Technology update(Integer technologyId, String technologyName) {
		if (technologyId == null || technologyId.equals(0)) {
			throw new InvalidInputException("Technology ID is not valid.");
		}		
		if (technologyName == null || technologyName.isBlank() || technologyName.isEmpty()) {
			throw new InvalidInputException("Technology name is not valid.");
		}
		if (!existsById(technologyId)) {
			throw new IndexDoesNotExistException("Technology ID does not exists.");
		}
		Technology oldTechnology = technologyDAL.findById(technologyId).get();
		if (technologyName != oldTechnology.getTechnologyName() && existsByTechnologyName(technologyName)) {
			throw new TechnologyException("Technology name already exists.");
		}
		
		Technology technology = new Technology(technologyId, technologyName);
		return technologyDAL.save(technology);
	}

	
	/** 
	 * @param technologyId
	 */
	@Override
	public void delete(Integer technologyId) {
		technologyDAL.deleteById(technologyId);
	}
	
	
	/** 
	 * @param technologyName
	 * @return Technology
	 */
	// Currently no mapping of this method because there is no need for it (yet)
	@Override
	public Technology getByTechnologyName(String technologyName) {
		if (technologyName == null || technologyName.isBlank() || technologyName.isEmpty()) {
			throw new InvalidInputException("Technology name is not valid.");
		}
		
		Optional<Technology> technology = technologyDAL.findByTechnologyName(technologyName);
		technology.orElseThrow(() -> new NullPointerException("Technology does not exist."));
		return technology.get();
	}

	
	/** 
	 * @return List<Technology>
	 */
	@Override
	public List<Technology> getAll() {
		return technologyDAL.findAll();
	}

	
	/** 
	 * @param technologyId
	 * @return boolean
	 */
	@Override
	public boolean existsById(Integer technologyId) {
		return technologyDAL.existsById(technologyId);
	}

	
	/** 
	 * @param technologyName
	 * @return boolean
	 */
	@Override
	public boolean existsByTechnologyName(String technologyName) {
		return !technologyDAL.findByTechnologyName(technologyName).isEmpty();
	}

}
