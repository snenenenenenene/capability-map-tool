package com.bavostepbros.leap.controller;

import com.bavostepbros.leap.LeapApplication;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Templatecontroller {

    @Autowired
    CapabilityService capabilityService;

    @Autowired
    EnvironmentService environmentService;

    @Autowired
    StatusService statusService;

    private ObjectMapper mapper = new ObjectMapper();


    @PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
    @GetMapping(path = "template/{templateName}")
    public String loadTemplate(
            @PathVariable("templateName") String templateName) {
        try {
            // lOAD TEMPLATE
            String templateDir = "../../../capability-map-templates/";
            File file = new ClassPathResource(templateDir + templateName + ".json", LeapApplication.class).getFile();

//            InputStream content = Files.newInputStream(file.toPath());
            String contents = new String(Files.readAllBytes(file.toPath()));

            // new environment
            environmentService.save("Template 1");
            // new status
            statusService.save(LocalDate.of(1, 1, 1));
            // SAVE ALL CAPS
            TypeReference<HashMap<String, String>> typeRef
                    = new TypeReference<HashMap<String, String>>() {};
            Map<String, String> map = mapper.readValue(contents, typeRef);
//
//            loadCapabilities(map).forEach(
//                    i -> capabilityService.save(i)
//            );



        } catch (FileNotFoundException e) {
            //TODO fix catches
            System.out.println("sioepke");
        } catch (IOException e) {
            System.out.println("sebonki" + e.getMessage());
        }
        return null;
    }

//    private List<Capability> loadCapabilities(Map<String, String> contents) {
//
//    }

//    private Capability loadCapability(JsonElement)

}
