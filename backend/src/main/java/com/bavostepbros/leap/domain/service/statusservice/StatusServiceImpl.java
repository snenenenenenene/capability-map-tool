package com.bavostepbros.leap.domain.service.statusservice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.StatusException;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.persistence.StatusDAL;

import lombok.RequiredArgsConstructor;

/**
*
* @author Bavo Van Meel
*
*/
@Service
@Transactional
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
	
	@Autowired
    private StatusDAL statusDAL;

    @Override
    public Status save(LocalDate validityPeriod) {
    	if (validityPeriod == null) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!existsByValidityPeriod(validityPeriod)) {
			throw new DuplicateValueException("Validity period already exists.");
		}
		
        Status status = new Status(validityPeriod);
        Status savedStatus = statusDAL.save(status);
        return savedStatus;
    }

    @Override
    public Status get(Integer id) {
    	if (id == null || id.equals(0)) {
			throw new InvalidInputException("Status ID is not valid.");
		}
		if (!existsById(id)) {
			throw new IndexDoesNotExistException("Status ID does not exists.");
		}
		
        Status status = statusDAL.findById(id).get();
        return status;
    }

    @Override
    public List<Status> getAll() {
        List<Status> status = new ArrayList<Status>();
        status = statusDAL.findAll();
        return status;
    }

    @Override
    public Status update(Integer statusId, LocalDate validityPeriod) {
    	if (statusId == null || statusId.equals(0) || validityPeriod == null) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!existsById(statusId)) {
			throw new IndexDoesNotExistException("Can not update status if it does not exist.");
		}
		if (existsByValidityPeriod(validityPeriod)) {
			throw new StatusException("Validity period already exists.");
		}
		
    	Status status = new Status(statusId, validityPeriod);
        Status updatedStatus = statusDAL.save(status);
        return updatedStatus;
    }

    @Override
    public void delete(Integer id) {
    	if (id == null || id.equals(0)) {
			throw new InvalidInputException("Status ID is not valid.");
		}
		if (!existsById(id)) {
			throw new IndexDoesNotExistException("Status ID does not exists.");
		}
		
        statusDAL.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {    	
        return statusDAL.existsById(id);
    }

    @Override
    public boolean existsByValidityPeriod(LocalDate validityPeriod) {
        return statusDAL.findByValidityPeriod(validityPeriod).isEmpty();
    }

}
