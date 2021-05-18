package com.bavostepbros.leap.controller;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.service.itapplicationservice.ITApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/itapplication")
public class ITApplicationController {

    @Autowired
    private ITApplicationService itApplicationService;

    @PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addITApplication(
            @ModelAttribute("statusId") Integer statusID,
            @ModelAttribute("name") String name,
            @ModelAttribute("technology") String technology,
            @ModelAttribute("version") String version,
            @ModelAttribute("purchaseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate,
            @ModelAttribute("endOfLife") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endOfLife,
            @ModelAttribute("currentScalability") Integer currentScalability,
            @ModelAttribute("expectedScalability") Integer expectedScalability,
            @ModelAttribute("currentPerformance") Integer currentPerformance,
            @ModelAttribute("expectedPerformance") Integer expectedPerformance,
            @ModelAttribute("currentSecurityLevel") Integer currentSecurityLevel,
            @ModelAttribute("expectedSecurityLevel") Integer expectedSecurityLevel,
            @ModelAttribute("currentStability") Integer currentsStability,
            @ModelAttribute("expectedStability") Integer expectedStability,
            @ModelAttribute("costCurrency") String costCurrency,
            @ModelAttribute("currentValue") String currentValue,
            @ModelAttribute("currentYearlyCost") Double currentYearlyCost,
            @ModelAttribute("timeValue") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeValue,
            UriComponentsBuilder builder) {
        if(itApplicationService.existsByName(name)) throw new DuplicateValueException("An application already exists with this name.");
        long applicationId = itApplicationService.save(statusID, name, technology, version, purchaseDate, endOfLife,
                currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
                expectedSecurityLevel, currentsStability, expectedStability, costCurrency, currentValue,
                currentYearlyCost, timeValue).getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/get/{id}").buildAndExpand(applicationId).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<ITApplication> getITApplication(@RequestParam Integer id){
        if(!itApplicationService.existsById(id)) throw new IndexDoesNotExistException("Application not found.");
        return new ResponseEntity<ITApplication>(itApplicationService.get(id), HttpStatus.OK);
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<List<ITApplication>> getITApplicationByName(@RequestParam String name){
        return new ResponseEntity<List<ITApplication>>(itApplicationService.get(name), HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<ITApplication>> getAllITApplications(){
        return new ResponseEntity<>(itApplicationService.getAll(), HttpStatus.OK);
    }

    @PutMapping(path = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateITApplication(
            @ModelAttribute("id") long id,
            @ModelAttribute("statusId") Integer statusID,
            @ModelAttribute("name") String name,
            @ModelAttribute("technology") String technology,
            @ModelAttribute("version") String version,
            @ModelAttribute("purchaseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate,
            @ModelAttribute("endOfLife") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endOfLife,
            @ModelAttribute("currentScalability") Integer currentScalability,
            @ModelAttribute("expectedScalability") Integer expectedScalability,
            @ModelAttribute("currentPerformance") Integer currentPerformance,
            @ModelAttribute("expectedPerformance") Integer expectedPerformance,
            @ModelAttribute("currentSecurityLevel") Integer currentSecurityLevel,
            @ModelAttribute("expectedSecurityLevel") Integer expectedSecurityLevel,
            @ModelAttribute("currentStability") Integer currentsStability,
            @ModelAttribute("expectedStability") Integer expectedStability,
            @ModelAttribute("costCurrency") String costCurrency,
            @ModelAttribute("currentValue") String currentValue,
            @ModelAttribute("currentYearlyCost") Double currentYearlyCost,
            @ModelAttribute("timeValue") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeValue) {
        ITApplication updatedITApplication = itApplicationService.update(id, statusID, name, technology, version, purchaseDate, endOfLife,
                currentScalability, expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
                expectedSecurityLevel, currentsStability, expectedStability, costCurrency,
                currentValue, currentYearlyCost, timeValue);

        return new ResponseEntity<>(updatedITApplication, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteITApplication(@RequestParam Integer id){
        if(id == null || id.equals(0)) throw new InvalidInputException("Invalid input exception");
        if (!itApplicationService.existsById(id)) throw new IndexDoesNotExistException("ID does not exist.");
        itApplicationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
