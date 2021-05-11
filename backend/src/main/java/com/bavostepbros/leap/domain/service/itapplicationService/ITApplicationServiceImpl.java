package com.bavostepbros.leap.domain.service.itapplicationService;

import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.persistence.ITApplicationDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
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
    private StatusDAL statusDAL;

    public ITApplication save(Integer statusId, String name, String technology, String version, LocalDate purchaseDate, LocalDate endOfLife, Byte currentScalability, Byte expectedScalability, Byte currentPerformance, Byte expectedPerformance, Byte currentSecurityLevel, Byte expectedSecurityLevel, Byte currentStability, Byte expectedStability, String costCurrency, String currentValue, Double currentYearlyCost, LocalDate timeValue) {
        return itApplicationDAL.save(new ITApplication(statusDAL.findById(statusId).get(), name, technology, version, purchaseDate, endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, costCurrency, currentValue, currentYearlyCost, timeValue));
    }

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

    public List<ITApplication> get(String name) {
        try {
            return itApplicationDAL.findByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ITApplication> getAll() {
        try {
            return itApplicationDAL.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ITApplication update(Integer statusID, String name, String technology, String version, LocalDate purchaseDate, LocalDate endOfLife, Byte currentScalability, Byte expectedScalability, Byte currentPerformance, Byte expectedPerformance, Byte currentSecurityLevel, Byte expectedSecurityLevel, Byte currentStability, Byte expectedStability, String costCurrency, String currentValue, Double currentYearlyCost, LocalDate timeValue) {
        try {
            return itApplicationDAL.save(new ITApplication(statusDAL.findById(statusID).get(), name, technology, version, purchaseDate, endOfLife, currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel, expectedSecurityLevel, currentStability, expectedStability, costCurrency, currentValue, currentYearlyCost, timeValue));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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
        return itApplicationDAL.findByName(name).isEmpty();
    }
}
