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
import org.springframework.security.access.prepost.PreAuthorize;
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

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StrategyDto addStrategy(@ModelAttribute("statusId") Integer statusId,
			@ModelAttribute("strategyName") String strategyName,
			@ModelAttribute("timeFrameStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameStart,
			@ModelAttribute("timeFrameEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate timeFrameEnd,
			@ModelAttribute("environmentId") Integer environmentId) {

		Strategy strategy = strategyService.save(statusId, strategyName, timeFrameStart, timeFrameEnd, environmentId);
		return convertStrategy(strategy);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "{strategyid}")
	public StrategyDto getStrategyById(@PathVariable("strategyid") Integer id) {
		Strategy strategy = strategyService.get(id);
		return convertStrategy(strategy);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "strategyname/{strategyname}")
	public StrategyDto getStrategyByStrategyname(@PathVariable("strategyname") String strategyName) {
		Strategy strategy = strategyService.getStrategyByStrategyname(strategyName);
		return convertStrategy(strategy);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "all-strategies-by-environmentid/{environmentid}")
	public List<StrategyDto> getAllCapabilitiesByEnvironment(@PathVariable("environmentid") Integer id) {
		List<Strategy> strategies = strategyService.getStrategiesByEnvironment(id);
		List<StrategyDto> strategiesDto = strategies.stream()
				.map(strategy -> convertStrategy(strategy))
				.collect(Collectors.toList());
		return strategiesDto;
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping
	public List<StrategyDto> getAllStrategies() {
		List<Strategy> strategies = strategyService.getAll();
		List<StrategyDto> strategiesDto = strategies.stream()
				.map(strategy -> convertStrategy(strategy))
				.collect(Collectors.toList());
		return strategiesDto;
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "exists-by-id/{strategyid}")
	public boolean doesStrategyExistsById(@PathVariable("strategyid") Integer id) {
		return strategyService.existsById(id);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "exists-by-strategyname/{strategyname}")
	public boolean doesStrategyNameExists(@PathVariable("strategyname") String strategyName) {
		return strategyService.existsByStrategyName(strategyName);
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
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

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "{strategyid}")
	public void deleteStrategy(@PathVariable("strategyid") Integer id) {
		strategyService.delete(id);
	}

	private StrategyDto convertStrategy(Strategy strategy) {
		StatusDto status = new StatusDto(strategy.getStatus().getStatusId(), strategy.getStatus().getValidityPeriod());
		EnvironmentDto environment = new EnvironmentDto(strategy.getEnvironment().getEnvironmentId(),
				strategy.getEnvironment().getEnvironmentName());
		return new StrategyDto(strategy.getStrategyId(), status, strategy.getStrategyName(),
				strategy.getTimeFrameStart(), strategy.getTimeFrameEnd(), environment);
	}
}
