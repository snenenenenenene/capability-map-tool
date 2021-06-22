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

@Service
@Transactional
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
	
	@Autowired
    private StatusDAL statusDAL;


    /**
     * @param validityPeriod
     * @return Status
     */
    @Override
    public Status save(@NotNull LocalDate validityPeriod) {
        Status status = new Status(validityPeriod);
        return statusDAL.save(status);
    }


    /**
     * @param id
     * @return Status
     */
    @Override
    public Status get(Integer id) {
        Status status = statusDAL.findById(id).get();
        return status;
    }


    /**
     * @return List<Status>
     */
    @Override
    public List<Status> getAll() {
        return statusDAL.findAll();
    }


    /**
     * @param statusId
     * @param validityPeriod
     * @return Status
     */
    @Override
    public Status update(Integer statusId, @NotNull LocalDate validityPeriod) {
    	Status status = new Status(statusId, validityPeriod);
        return statusDAL.save(status);
    }


    /**
     * @param id
     */
    @Override
    public void delete(Integer id) {
        statusDAL.deleteById(id);
    }


    /**
     * @param id
     * @return boolean
     */
    @Override
    public boolean existsById(Integer id) {    	
        return statusDAL.existsById(id);
    }


    /**
     * @param validityPeriod
     * @return boolean
     */
    @Override
    public boolean existsByValidityPeriod(@NotNull LocalDate validityPeriod) {
        return !statusDAL.findByValidityPeriod(validityPeriod).isEmpty();
    }

    /**
     * @param validityPeriod
     * @return Status
     */
    @Override
    public Status getByValidityPeriod(@NotNull LocalDate validityPeriod) {
        return statusDAL.findByValidityPeriod(validityPeriod).get();
    }

}
