package com.bavostepbros.leap.domain.service.statusservice;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.persistence.StatusDAL;

@Service
@Transactional
public class StatusServiceImpl implements StatusService {

    private final StatusDAL statusDAL;

    @Autowired
    public StatusServiceImpl(StatusDAL statusDAL) {
        super();
        this.statusDAL = statusDAL;
    }

    @Override
    public boolean save(Status status) {
        try {
            statusDAL.save(status);
            return true;
        } catch (Exception e) {
            return false;
        }
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
    public void update(Status status) {
        statusDAL.save(status);
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
    public boolean existsByValidityPeriod(Integer validityPeriod) {
        boolean result = statusDAL.findByValidityPeriod(validityPeriod).isEmpty();
        return result;
    }

}
