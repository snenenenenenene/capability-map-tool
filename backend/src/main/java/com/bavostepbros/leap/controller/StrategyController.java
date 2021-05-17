package com.bavostepbros.leap.controller;

import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.service.StrategyService.StrategyService;

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

/**
 *
 * @author Lenny Bontenakel, Bavo Van Meel
 *
 */
// @CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/strategy/")
public class StrategyController {

	@Autowired
	private StrategyService strategyService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> addStrategy(
			@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("strategyName") String strategyName,
			@ModelAttribute("timeFrameStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameStart,
			@ModelAttribute("timeFrameEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameEnd,
			@ModelAttribute("environmentId") Integer environmentId, UriComponentsBuilder builder) {

		Strategy strategy = strategyService.save(statusId, strategyName, timeFrameStart, timeFrameEnd, environmentId);
		Integer strategyId = strategy.getStrategyId();
		boolean flag = (strategyId == null) ? false : true;
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		LocalDate ld = LocalDate.now();
		strategy.setTimeFrameEnd(ld);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("{id}").buildAndExpand(strategyId).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	@GetMapping(path = "{strategyid}")
	public ResponseEntity<Strategy> getStrategyById(@PathVariable("strategyid") Integer id) {
		Strategy strategy = strategyService.get(id);
		return new ResponseEntity<Strategy>(strategy, HttpStatus.OK);
	}

	@GetMapping(path = "all-by-environmentid/{environmentid}")
	public ResponseEntity<List<Strategy>> getAllCapabilitiesByEnvironment(@PathVariable("environmentid") Integer id) {
		List<Strategy> strategies = strategyService.getStrategiesByEnvironment(id);
		return new ResponseEntity<List<Strategy>>(strategies, HttpStatus.OK);
	}

	@GetMapping
	public List<Strategy> getAllStrategies() {
		List<Strategy> strategies = strategyService.getAll();
		return strategies;
	}

	@GetMapping(path = "exists-by-id/{strategyid}")
	public ResponseEntity<Boolean> doesStrategyExistsById(@PathVariable("strategyid") Integer id) {
		boolean result = strategyService.existsById(id);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@GetMapping(path = "exists-by-strategyname/{strategyname}")
	public ResponseEntity<Boolean> doesStrategyNameExists(@PathVariable("strategyname") String strategyName) {
		boolean result = (!strategyService.existsByStrategyName(strategyName));
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Strategy> updateStrategy(
			@ModelAttribute("strategyId") Integer strategyId,
			@ModelAttribute("statusId") Integer statusId, 
			@ModelAttribute("strategyName") String strategyName,
			@ModelAttribute("timeFrameStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameStart,
			@ModelAttribute("timeFrameEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameEnd,
			@ModelAttribute("environmentId") Integer environmentId) {

		Strategy strategy = strategyService.update(strategyId, statusId, strategyName, timeFrameStart, timeFrameEnd,
				environmentId);
		return new ResponseEntity<Strategy>(strategy, HttpStatus.OK);
	}

	@DeleteMapping("{strategyid}")
	public ResponseEntity<Void> deleteStrategy(@PathVariable("strategyid") Integer id) {
		strategyService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
