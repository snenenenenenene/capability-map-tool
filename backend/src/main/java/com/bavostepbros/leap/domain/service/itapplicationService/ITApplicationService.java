package com.bavostepbros.leap.domain.service.itapplicationService;

import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.model.Status;

import java.time.LocalDate;
import java.util.List;

public interface ITApplicationService {
    ITApplication save(Status status, String name, String technology, String version, LocalDate purchaseDate, LocalDate endOfLife, Byte currentScalability, Byte expectedScalability, Byte currentPerformance, Byte expectedPerformance, Byte currentSecurityLevel, Byte expectedSecurityLevel, Byte currentStability, Byte expectedStability, String costCurrency, String currentValue, Double currentYearlyCost, LocalDate timeValue);
    ITApplication get(Integer itapplicationID);
    List<ITApplication> get(String name);
    List<ITApplication> getAll();
    ITApplication update(Status status, String name, String technology, String version, LocalDate purchaseDate, LocalDate endOfLife, Byte currentScalability, Byte expectedScalability, Byte currentPerformance, Byte expectedPerformance, Byte currentSecurityLevel, Byte expectedSecurityLevel, Byte currentStability, Byte expectedStability, String costCurrency, String currentValue, Double currentYearlyCost, LocalDate timeValue);
    void delete(Integer itapplicationID);
    boolean existsById(Integer itapplicationID);
    boolean existsByName(String name);

}
