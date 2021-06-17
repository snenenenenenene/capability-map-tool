package com.bavostepbros.leap.controller;


import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/csv")
public class CsvController {

    @Autowired
    private CapabilityService capabilityService;

    @Autowired
    private EnvironmentService environmentService;

    @Autowired
    private StatusService statusService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadCsvFile(
            @ModelAttribute("file") MultipartFile file,
            @ModelAttribute("environmentId") Integer environmentId) {
        if(file.isEmpty()) return;
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVReader csvReader = new CSVReader(reader);

            Environment environment = environmentService.get(environmentId);
            Status status = statusService.save(LocalDate.of(1, 1, 1));
            String[] currLine;
            while ((currLine = csvReader.readNext()) != null) {
                try {
                    //TODO parse headers for indexes/ independent ordering
                    capabilityService.save(new Capability(
                            Integer.parseInt(currLine[0]),
                            environment,
                            status,
                            Integer.parseInt(currLine[1])
                    ));
                } catch (NumberFormatException | DuplicateValueException | ForeignKeyException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}
