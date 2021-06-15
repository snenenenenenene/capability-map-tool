package com.bavostepbros.leap.domain.service.statusservice;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Status status = new Status(validityPeriod);
        return statusDAL.save(status);
    }

    @Override
    public Status get(Integer id) {
        Status status = statusDAL.findById(id).get();
        return status;
    }

    @Override
    public List<Status> getAll() {
        return statusDAL.findAll();
    }

    @Override
    public Status update(Integer statusId, @NotNull LocalDate validityPeriod) {
    	Status status = new Status(statusId, validityPeriod);
        return statusDAL.save(status);
    }

    @Override
    public void delete(Integer id) {
        statusDAL.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {    	
        return statusDAL.existsById(id);
    }

    @Override
    public boolean existsByValidityPeriod(@NotNull LocalDate validityPeriod) {
        return !statusDAL.findByValidityPeriod(validityPeriod).isEmpty();
    }
    
    @Override
    public Status getByValidityPeriod(@NotNull LocalDate validityPeriod) {
        return statusDAL.findByValidityPeriod(validityPeriod).get();
    }

}
