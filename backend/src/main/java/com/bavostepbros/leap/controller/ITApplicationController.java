package com.bavostepbros.leap.controller;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.service.itapplicationService.ITApplicationService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/itapplication/")
public class ITApplicationController {

    @Autowired
    private ITApplicationService itApplicationService;
    private StatusService statusService;

    @PostMapping(path = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addITApplication(
            @ModelAttribute Integer statusID,
            @ModelAttribute String name,
            @ModelAttribute String technology,
            @ModelAttribute String version,
            @ModelAttribute LocalDate purchaseDate,
            @ModelAttribute LocalDate endOfLife,
            @ModelAttribute Byte currentScalability,
            @ModelAttribute Byte expectedScalability,
            @ModelAttribute Byte currentPerformance,
            @ModelAttribute Byte expectedPerformance,
            @ModelAttribute Byte currentSecurityLevel,
            @ModelAttribute Byte expectedSecurityLevel,
            @ModelAttribute Byte currentsStability,
            @ModelAttribute Byte expectedStability,
            @ModelAttribute String costCurrency,
            @ModelAttribute String currentValue,
            @ModelAttribute Double currentYearlyCost,
            @ModelAttribute LocalDate timeValue,
            UriComponentsBuilder builder) {
        if(name == null) throw new InvalidInputException("Invalid input.");
        if(itApplicationService.existsByName(name)) throw new DuplicateValueException("An application already exists with this name.");
        Integer applicationId = itApplicationService.save(statusService.get(statusID), name, technology, version, purchaseDate, endOfLife, currentScalability,
                expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
                expectedSecurityLevel, currentsStability, expectedStability, costCurrency,
                currentValue, currentYearlyCost, timeValue).getId();
        if(applicationId == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

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
    public ResponseEntity<Void> updateITApplication(@ModelAttribute Integer statusID,
                                              @ModelAttribute String name,
                                              @ModelAttribute String technology,
                                              @ModelAttribute String version,
                                              @ModelAttribute LocalDate purchaseDate,
                                              @ModelAttribute LocalDate endOfLife,
                                              @ModelAttribute Byte currentScalability,
                                              @ModelAttribute Byte expectedScalability,
                                              @ModelAttribute Byte currentPerformance,
                                              @ModelAttribute Byte expectedPerformance,
                                              @ModelAttribute Byte currentSecurityLevel,
                                              @ModelAttribute Byte expectedSecurityLevel,
                                              @ModelAttribute Byte currentsStability,
                                              @ModelAttribute Byte expectedStability,
                                              @ModelAttribute String costCurrency,
                                              @ModelAttribute String currentValue,
                                              @ModelAttribute Double currentYearlyCost,
                                              @ModelAttribute LocalDate timeValue,
                                              UriComponentsBuilder builder) {
        if(name == null) throw new InvalidInputException("Invalid input.");
        if(itApplicationService.existsByName(name)) throw new DuplicateValueException("An application already exists with this name.");
        Integer applicationId = itApplicationService.save(statusService.get(statusID), name, technology, version, purchaseDate, endOfLife, currentScalability,
                expectedScalability, currentPerformance, expectedPerformance, currentSecurityLevel,
                expectedSecurityLevel, currentsStability, expectedStability, costCurrency,
                currentValue, currentYearlyCost, timeValue).getId();
        if(applicationId == null) return new ResponseEntity<>(HttpStatus.CONFLICT);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/get/{id}").buildAndExpand(applicationId).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteITApplication(@RequestParam Integer id){
        if(id == null || id.equals(0)) throw new InvalidInputException("Invalid input exception");
        if (!itApplicationService.existsById(id)) throw new IndexDoesNotExistException("Capability ID does not exists.");

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
