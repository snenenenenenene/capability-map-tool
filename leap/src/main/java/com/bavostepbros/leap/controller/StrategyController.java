package com.bavostepbros.leap.controller;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.service.StrategyService.StrategyService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;

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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private EnvironmentService envService;

    @Autowired
    private StatusService statusService;

    @PostMapping(path = "/strategy/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addStrategy(
            @ModelAttribute("statusId") Integer statusId,
            @ModelAttribute("validityPeriod") Integer validityPeriod,
            @ModelAttribute("strategyName") String strategyName,
			@ModelAttribute("timeFrameStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameStart,
			@ModelAttribute("timeFrameEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameEnd,
            UriComponentsBuilder builder) {
    	if (statusId == null || statusId.equals(0) || validityPeriod == null || validityPeriod.equals(0)
    			|| strategyName == null || strategyName.isBlank() || strategyName.isEmpty()) {
    		throw new InvalidInputException("Invalid input.");
    	}
    	if (!strategyService.existsByStrategyName(strategyName)) {
			throw new DuplicateValueException("Strategy name already exists.");
		}

		System.out.println();
		System.out.println("\n\n\nTIMEFRAMEEND:\n\n\n " + timeFrameEnd);
		System.out.println("\n\n\nTIMEFRAMESTART:\n\n\n " + timeFrameStart);
    	
        Strategy strategy = strategyService.save(statusId, validityPeriod, strategyName, timeFrameStart, timeFrameEnd);
        Integer strategyId = strategy.getStrategyId();
        boolean flag = (strategyId == null) ? false : true;
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        LocalDate ld = LocalDate.now();
        System.out.println(ld);

        System.out.println(strategy);
        strategy.setTimeFrameEnd(ld);
        System.out.println(strategy.getTimeFrameEnd());
        System.out.println(strategy);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder
                .path("/strategy/{id}")
                .buildAndExpand(strategyId).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @GetMapping(path = "/strategy/{id}")
    public ResponseEntity<Strategy> getStrategyById(@PathVariable("id") Integer id) {
    	if (id == null || id.equals(0)) {
			throw new InvalidInputException("Strategy ID is not valid.");
		}
		if (!strategyService.existsById(id)) {
			throw new IndexDoesNotExistException("Strategy ID does not exists.");
		}

    	Strategy strategy = strategyService.get(id);
        return  new ResponseEntity<Strategy>(strategy, HttpStatus.OK);
    }

    @GetMapping(path = "/strategy/all")
    public List<Strategy> getAllStrategies() {
        List<Strategy> strategies = strategyService.getAll();
        return strategies;
    }

    @GetMapping(path = "/strategy/exists/id/{id}")
	public ResponseEntity<Boolean> doesStrategyExistsById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Strategy ID is not valid.");
		}

		boolean result = strategyService.existsById(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

    @GetMapping(path = "/strategy/exists/strategyname/{strategyname}")
	public ResponseEntity<Boolean> doesStrategyNameExists(@PathVariable("strategyname") String strategyName) {
		if (strategyName == null ||
				strategyName.isBlank() ||
				strategyName.isEmpty()) {
			throw new InvalidInputException("Input is not valid.");
		}

		boolean result = (!strategyService.existsByStrategyName(strategyName));
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

    @PutMapping(path = "/strategy/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Strategy> updateStrategy(@ModelAttribute Strategy strategy) {
    	if (strategy.getStrategyId() == null ||
    			strategy.getStrategyId().equals(0) ||
    			strategy.getStrategyName() == null ||
    			strategy.getStrategyName().isBlank() ||
    			strategy.getStrategyName().isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!strategyService.existsById(strategy.getStrategyId())) {
			throw new IndexDoesNotExistException("Can not update capability if it does not exist.");
		}
		if (!strategyService.existsByStrategyName(strategy.getStrategyName())) {
			throw new DuplicateValueException("Capability name already exists.");
		}
		if (!envService.existsById(strategy.getEnvironment().getEnvironmentId())) {
			throw new IndexDoesNotExistException("Environment ID does not exists.");
		}
		if (envService.existsByEnvironmentName(strategy.getEnvironment().getEnvironmentName())) {
			throw new DuplicateValueException("Environment name does not exists.");
		}
		if (!statusService.existsById(strategy.getStatus().getStatusId())) {
			throw new IndexDoesNotExistException("Status ID does not exists.");
		}
		if (statusService.existsByValidityPeriod(strategy.getStatus().getValidityPeriod())) {
			throw new DuplicateValueException("Validity period does not exists.");
		}

    	strategyService.update(strategy);
        return new ResponseEntity<Strategy>(strategy, HttpStatus.OK);
    }

    @DeleteMapping("/strategy/delete/{id}")
    public ResponseEntity<Void> deleteStrategy(@PathVariable("id") Integer id) {
    	if (id == null || id.equals(0)) {
			throw new InvalidInputException("Strategy ID is not valid.");
		}
		if (!strategyService.existsById(id)) {
			throw new IndexDoesNotExistException("Strategy ID does not exists.");
		}

    	strategyService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
