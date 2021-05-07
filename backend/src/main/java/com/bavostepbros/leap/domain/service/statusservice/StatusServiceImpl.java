package com.bavostepbros.leap.domain.service.statusservice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.persistence.StatusDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
	
	@Autowired
    private StatusDAL statusDAL;

    @Override
    public Status save(LocalDate validityPeriod) {
        Status status = new Status(validityPeriod);
        Status savedStatus = statusDAL.save(status);
        return savedStatus;
    }

    @Override
    public Status get(Integer id) {
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
    	Status status = new Status(statusId, validityPeriod);
        Status updatedStatus = statusDAL.save(status);
        return updatedStatus;
    }

    @Override
    public void delete(Integer id) {
        statusDAL.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        boolean result = statusDAL.existsById(id);
        return result;
    }

    @Override
    public boolean existsByValidityPeriod(LocalDate validityPeriod) {
        boolean result = statusDAL.findByValidityPeriod(validityPeriod).isEmpty();
        return result;
    }

}
