package com.bavostepbros.leap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bavostepbros.leap.database.StrategyService;
import com.bavostepbros.leap.model.Strategy;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class StrategyController {

    @Autowired
    private StrategyService stratService;

    @PostMapping("/strategy/add")
    public ResponseEntity<Void> addStrategy(
            @RequestBody Strategy strategy,
            UriComponentsBuilder builder) {

        boolean flag = stratService.save(strategy);
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
        Strategy strategy = stratService.get(id);
        return  new ResponseEntity<Strategy>(strategy, HttpStatus.OK);
    }

    @GetMapping("/strategy/all")
    public List<Strategy> getAllStrategies() {
        List<Strategy> strategies = stratService.getAll();
        return strategies;
    }

    @PutMapping("/strategy/update")
    public ResponseEntity<Strategy> updateStrategy(@RequestBody Strategy strategy) {
        stratService.update(strategy);
        return new ResponseEntity<Strategy>(strategy, HttpStatus.OK);
    }

    @DeleteMapping("/strategy/{id}")
    public ResponseEntity<Void> deleteStrategy(@PathVariable("id") Integer id) {
        stratService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
