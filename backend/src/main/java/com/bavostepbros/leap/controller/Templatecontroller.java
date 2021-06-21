package com.bavostepbros.leap.controller;

import com.bavostepbros.leap.LeapApplication;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Templatecontroller {

    @Autowired
    CapabilityService capabilityService;

    @Autowired
    EnvironmentService environmentService;

    @Autowired
    StatusService statusService;

    String TEMPLATEDIR = "../../../capability-map-templates/";

    @PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
    @GetMapping(path = "template/{templateName}")
    public void loadTemplate(
            @PathVariable("templateName") String templateName) {
        try {
            File file = new ClassPathResource(TEMPLATEDIR + templateName + ".json", LeapApplication.class).getFile();
            String contentString = new String(Files.readAllBytes(file.toPath()));
            JSONObject json = new JSONObject(contentString);

            Environment environment = environmentService.save(json.getString("environmentName"));

            Status status = statusService.save(LocalDate.of(1, 1, 1));

            JSONArray capabilities = json.getJSONArray("capabilities");
            loadCapabilities(capabilities, 0, environment, status);
        } catch (IOException e) {
            //TODO fix catches
            System.out.println("sioepke");
        }
    }

    private void loadCapabilities(JSONArray capabilities, Integer parentId,  Environment environment, Status status) {
        final int length = capabilities.length();
        for (int index = 0; index < length; index++) {
            final JSONObject capabilityData = capabilities.getJSONObject(index);
            Capability capability = new Capability(
                    capabilityData.getInt("capabilityId"),
                    environment,
                    status,
                    parentId,
                    capabilityData.getString("capabilityName"),
                    null,
                    PaceOfChange.STANDARD,
                    TargetOperatingModel.COORDINATION,
                    1,
                    null,
                    null
            );
            capabilityService.save(capability);
            JSONArray children = capabilityData.getJSONArray("children");
            if (!children.isEmpty()) loadCapabilities(children, capability.getCapabilityId(), environment, status);
        }
    }
}
