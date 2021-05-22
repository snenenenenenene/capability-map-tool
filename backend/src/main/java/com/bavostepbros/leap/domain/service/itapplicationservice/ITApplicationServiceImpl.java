package com.bavostepbros.leap.domain.service.itapplicationservice;

import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.ITApplicationDAL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ITApplicationServiceImpl implements ITApplicationService {

    @Autowired
    private ITApplicationDAL itApplicationDAL;

    @Autowired
    private StatusService statusService;

    @Override
    public ITApplication save(Integer statusId, String name, String technology, String version, LocalDate purchaseDate, LocalDate endOfLife, Integer currentScalability, Integer expectedScalability, Integer currentPerformance, Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel, Integer currentStability, Integer expectedStability, String costCurrency, String currentValue, Double currentYearlyCost, LocalDate timeValue) {
        if (!statusService.existsById(statusId)) {
            throw new ForeignKeyException("Status ID does not exists.");
        }

        return itApplicationDAL.save(new ITApplication(statusService.get(statusId), name, technology, version, purchaseDate, endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, costCurrency, currentValue, currentYearlyCost, timeValue));
    }

    @Override
    public ITApplication save(ITApplication itApplication) {
        return itApplicationDAL.save(itApplication);
    }

    public ITApplication get(Integer itapplicationID) {
        try {
            return itApplicationDAL.findById(itapplicationID).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ITApplication> get(String name) {
        try {
            return itApplicationDAL.findByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ITApplication> getAll() {
        try {
            return itApplicationDAL.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ITApplication update(Integer id, Integer statusID, String name, String technology, String version, LocalDate purchaseDate, LocalDate endOfLife, Integer currentScalability, Integer expectedScalability, Integer currentPerformance, Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel, Integer currentStability, Integer expectedStability, String costCurrency, String currentValue, Double currentYearlyCost, LocalDate timeValue) {
        try {
            ITApplication itApplication = new ITApplication(statusService.get(statusID), name, technology, version, purchaseDate, endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, costCurrency, currentValue, currentYearlyCost, timeValue);
            itApplication.setItApplicationId(id);
            return itApplicationDAL.save(itApplication);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(Integer id){
        try {
            itApplicationDAL.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean existsById(Integer id) {
        return itApplicationDAL.existsById(id);
    }

    public boolean existsByName(String name) {
        return !itApplicationDAL.findByName(name).isEmpty();
    }
}
