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

// @CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/strategy/")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private StatusService statusService;
    
    @Autowired
	private EnvironmentService envService;

    @PostMapping(path = "add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addStrategy(
            @ModelAttribute("statusId") Integer statusId,
            @ModelAttribute("validityPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  validityPeriod,
            @ModelAttribute("strategyName") String strategyName,
			@ModelAttribute("timeFrameStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameStart,
			@ModelAttribute("timeFrameEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameEnd,
			@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("environmentName") String environmentName,
            UriComponentsBuilder builder) {
    	if (statusId == null || statusId.equals(0) || validityPeriod == null || strategyName == null 
    			|| strategyName.isBlank() || strategyName.isEmpty()) {
    		throw new InvalidInputException("Invalid input.");
    	}
    	if (!strategyService.existsByStrategyName(strategyName)) {
			throw new DuplicateValueException("Strategy name already exists.");
		}
    	
        Strategy strategy = strategyService.save(statusId, validityPeriod, strategyName, timeFrameStart, 
        		timeFrameEnd, environmentId, environmentName);
        Integer strategyId = strategy.getStrategyId();
        boolean flag = (strategyId == null) ? false : true;
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        LocalDate ld = LocalDate.now();
        strategy.setTimeFrameEnd(ld);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder
                .path("/strategy/{id}")
                .buildAndExpand(strategyId).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @GetMapping(path = "{id}")
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
    
    @GetMapping(path = "getallbyenvironment/{id}")
	public ResponseEntity<List<Strategy>> getAllCapabilitiesByEnvironment(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Environment ID is not valid.");
		}
		if (!envService.existsById(id)) {
			throw new IndexDoesNotExistException("Environment ID does not exists.");
		}

		List<Strategy> strategies = strategyService.getStrategiesByEnvironment(id);
		return new ResponseEntity<List<Strategy>>(strategies, HttpStatus.OK);
	}

    @GetMapping(path = "all")
    public List<Strategy> getAllStrategies() {
        List<Strategy> strategies = strategyService.getAll();
        return strategies;
    }

    @GetMapping(path = "exists/id/{id}")
	public ResponseEntity<Boolean> doesStrategyExistsById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Strategy ID is not valid.");
		}

		boolean result = strategyService.existsById(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

    @GetMapping(path = "exists/strategyname/{strategyname}")
	public ResponseEntity<Boolean> doesStrategyNameExists(@PathVariable("strategyname") String strategyName) {
		if (strategyName == null ||
				strategyName.isBlank() ||
				strategyName.isEmpty()) {
			throw new InvalidInputException("Input is not valid.");
		}

		boolean result = (!strategyService.existsByStrategyName(strategyName));
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

    @PutMapping(path = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Strategy> updateStrategy(
    		@ModelAttribute("strategyId") Integer strategyId,
    		@ModelAttribute("statusId") Integer statusId,
            @ModelAttribute("validityPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate  validityPeriod,
            @ModelAttribute("strategyName") String strategyName,
			@ModelAttribute("timeFrameStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameStart,
			@ModelAttribute("timeFrameEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameEnd,
			@ModelAttribute("environmentId") Integer environmentId,
			@ModelAttribute("environmentName") String environmentName) {
    	if (strategyId == null || strategyId.equals(0) || strategyName == null || 
    			strategyName.isBlank() || strategyName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!strategyService.existsById(strategyId)) {
			throw new IndexDoesNotExistException("Can not update strategy if it does not exist.");
		}
		if (!strategyService.existsByStrategyName(strategyName)) {
			throw new DuplicateValueException("Strategy name already exists.");
		}
		if (!statusService.existsById(statusId)) {
			throw new IndexDoesNotExistException("Status ID does not exists.");
		}
		if (statusService.existsByValidityPeriod(validityPeriod)) {
			throw new DuplicateValueException("Validity period does not exists.");
		}

    	Strategy strategy = strategyService.update(strategyId, statusId, validityPeriod, 
    			strategyName, timeFrameStart, timeFrameEnd, environmentId, environmentName);
        return new ResponseEntity<Strategy>(strategy, HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
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
