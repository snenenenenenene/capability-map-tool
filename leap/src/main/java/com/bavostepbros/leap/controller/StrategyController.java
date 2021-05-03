package com.bavostepbros.leap.controller;

import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.service.StrategyService.StrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @PostMapping(path = "/strategy/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addStrategy(
            @ModelAttribute Strategy strategy,
            UriComponentsBuilder builder) {

        boolean flag = strategyService.save(strategy);
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder
                .path("/strategy/{id}")
                .buildAndExpand(strategy.getStrategyId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/strategy/{id}")
    public ResponseEntity<Strategy> getStrategyById(@PathVariable("id") Integer id) {
        Strategy strategy = strategyService.get(id);
        return  new ResponseEntity<Strategy>(strategy, HttpStatus.OK);
    }

    @GetMapping("/strategy/all")
    public List<Strategy> getAllStrategies() {
        List<Strategy> strategies = strategyService.getAll();
        return strategies;
    }

    @PutMapping("/strategy/update")
    public ResponseEntity<Strategy> updateStrategy(@RequestBody Strategy strategy) {
        strategyService.update(strategy);
        return new ResponseEntity<Strategy>(strategy, HttpStatus.OK);
    }

    @DeleteMapping("/strategy/{id}")
    public ResponseEntity<Void> deleteStrategy(@PathVariable("id") Integer id) {
        strategyService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
