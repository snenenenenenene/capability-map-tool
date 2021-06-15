package com.bavostepbros.leap.controller;

import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.model.dto.StrategyDto;
import com.bavostepbros.leap.domain.service.strategyservice.StrategyService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

	
	/** 
	 * @param @ModelAttribute("statusId"
	 * @return StrategyDto
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StrategyDto addStrategy(@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("strategyName") String strategyName,
			@ModelAttribute("timeFrameStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameStart,
			@ModelAttribute("timeFrameEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameEnd,
			@ModelAttribute("environmentId") Integer environmentId) {

		Strategy strategy = strategyService.save(statusId, strategyName, timeFrameStart, timeFrameEnd, environmentId);
		return convertStrategy(strategy);
	}

	
	/** 
	 * @param id
	 * @return StrategyDto
	 */
	@GetMapping(path = "{strategyid}")
	public StrategyDto getStrategyById(@PathVariable("strategyid") Integer id) {
		Strategy strategy = strategyService.get(id);
		return convertStrategy(strategy);
	}

	
	/** 
	 * @param strategyName
	 * @return StrategyDto
	 */
	@GetMapping(path = "strategyname/{strategyname}")
	public StrategyDto getStrategyByStrategyname(@PathVariable("strategyname") String strategyName) {
		Strategy strategy = strategyService.getStrategyByStrategyname(strategyName);
		return convertStrategy(strategy);
	}

	
	/** 
	 * @param id
	 * @return List<StrategyDto>
	 */
	@GetMapping(path = "all-strategies-by-environmentid/{environmentid}")
	public List<StrategyDto> getAllCapabilitiesByEnvironment(@PathVariable("environmentid") Integer id) {
		List<Strategy> strategies = strategyService.getStrategiesByEnvironment(id);
		List<StrategyDto> strategiesDto = strategies.stream()
				.map(strategy -> convertStrategy(strategy))
				.collect(Collectors.toList());
		return strategiesDto;
	}

	
	/** 
	 * @return List<StrategyDto>
	 */
	@GetMapping
	public List<StrategyDto> getAllStrategies() {
		List<Strategy> strategies = strategyService.getAll();
		List<StrategyDto> strategiesDto = strategies.stream()
				.map(strategy -> convertStrategy(strategy))
				.collect(Collectors.toList());
		return strategiesDto;
	}

	
	/** 
	 * @param id
	 * @return boolean
	 */
	@GetMapping(path = "exists-by-id/{strategyid}")
	public boolean doesStrategyExistsById(@PathVariable("strategyid") Integer id) {
		return strategyService.existsById(id);
	}

	
	/** 
	 * @param strategyName
	 * @return boolean
	 */
	@GetMapping(path = "exists-by-strategyname/{strategyname}")
	public boolean doesStrategyNameExists(@PathVariable("strategyname") String strategyName) {
		return strategyService.existsByStrategyName(strategyName);
	}

	
	/** 
	 * @param @PathVariable("strategyId"
	 * @return StrategyDto
	 */
	@PutMapping(path = "{strategyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StrategyDto updateStrategy(@PathVariable("strategyId") Integer strategyId,
			@ModelAttribute("statusId") Integer statusId, @ModelAttribute("strategyName") String strategyName,
			@ModelAttribute("timeFrameStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameStart,
			@ModelAttribute("timeFrameEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameEnd,
			@ModelAttribute("environmentId") Integer environmentId) {

		Strategy strategy = strategyService.update(strategyId, statusId, strategyName, timeFrameStart, timeFrameEnd,
				environmentId);
		return convertStrategy(strategy);
	}

	
	/** 
	 * @param id
	 */
	@DeleteMapping(path = "{strategyid}")
	public void deleteStrategy(@PathVariable("strategyid") Integer id) {
		strategyService.delete(id);
	}

	
	/** 
	 * @param strategy
	 * @return StrategyDto
	 */
	private StrategyDto convertStrategy(Strategy strategy) {
		StatusDto status = new StatusDto(strategy.getStatus().getStatusId(), strategy.getStatus().getValidityPeriod());
		EnvironmentDto environment = new EnvironmentDto(strategy.getEnvironment().getEnvironmentId(),
				strategy.getEnvironment().getEnvironmentName());
		return new StrategyDto(strategy.getStrategyId(), status, strategy.getStrategyName(),
				strategy.getTimeFrameStart(), strategy.getTimeFrameEnd(), environment);
	}
}
