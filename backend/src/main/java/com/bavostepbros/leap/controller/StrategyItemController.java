package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.model.dto.StrategyDto;
import com.bavostepbros.leap.domain.model.dto.StrategyItemDto;
import com.bavostepbros.leap.domain.service.strategyitemservice.StrategyItemService;

import lombok.RequiredArgsConstructor;

//@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/strategyitem/")
public class StrategyItemController {

	@Autowired
	private StrategyItemService strategyItemService;

	
	/** 
	 * @param @ModelAttribute("strategyId"
	 * @return StrategyItemDto
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StrategyItemDto addStrategyItem(@ModelAttribute("strategyId") Integer strategyId,
			@ModelAttribute("strategyItemName") String strategyItemName,
			@ModelAttribute("description") String description) {

		StrategyItem strategyItem = strategyItemService.save(strategyId, strategyItemName, description);
		return convertStrategyItem(strategyItem);
	}

	
	/** 
	 * @param itemId
	 * @return StrategyItemDto
	 */
	@GetMapping("{itemId}")
	public StrategyItemDto getStrategyItemByItemid(@PathVariable("itemId") Integer itemId) {

		StrategyItem strategyItem = strategyItemService.get(itemId);
		return convertStrategyItem(strategyItem);
	}

	
	/** 
	 * @param @PathVariable("itemId"
	 * @return StrategyItemDto
	 */
	@PutMapping(path = "{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StrategyItemDto updateStrategyItem(@PathVariable("itemId") Integer itemId,
			@ModelAttribute("strategyId") Integer strategyId,
			@ModelAttribute("strategyItemName") String strategyItemName,
			@ModelAttribute("description") String description) {

		StrategyItem strategyItem = strategyItemService.update(itemId, strategyId, strategyItemName, description);
		return convertStrategyItem(strategyItem);
	}

	
	/** 
	 * @param itemId
	 */
	@DeleteMapping(path = "{itemId}")
	public void deleteStrategyItem(@PathVariable("itemId") Integer itemId) {
		strategyItemService.delete(itemId);
	}

	
	/** 
	 * @return List<StrategyItemDto>
	 */
	@GetMapping
	public List<StrategyItemDto> getAllStrategyItems() {
		List<StrategyItem> strategyItems = strategyItemService.getAll();
		List<StrategyItemDto> strategyItemsDto = strategyItems.stream()
				.map(strategyItem -> convertStrategyItem(strategyItem)).collect(Collectors.toList());
		return strategyItemsDto;
	}

	
	/** 
	 * @param itemId
	 * @return boolean
	 */
	@GetMapping(path = "exists-by-id/{itemId}")
	public boolean doesStrategyItemExistsById(@PathVariable("itemId") Integer itemId) {
		return strategyItemService.existsById(itemId);
	}

	
	/** 
	 * @param strategyItemName
	 * @return boolean
	 */
	@GetMapping(path = "exists-by-strategyitemname/{strategyItemName}")
	public boolean doesStrategyItemExistsByStrategyitemname(@PathVariable("strategyItemName") String strategyItemName) {
		return strategyItemService.existsByStrategyItemName(strategyItemName);
	}

	
	/** 
	 * @param strategyItemName
	 * @return StrategyItemDto
	 */
	@GetMapping(path = "strategyitemname/{strategyItemName}")
	public StrategyItemDto getStrategyByStrategyName(@ModelAttribute("strategyItemName") String strategyItemName) {
		StrategyItem strategyItem = strategyItemService.getStrategyItemByStrategyItemName(strategyItemName);
		return convertStrategyItem(strategyItem);
	}

	
	/** 
	 * @param strategyId
	 * @return List<StrategyItemDto>
	 */
	@GetMapping(path = "all-strategyitems-by-strategyid/{strategyId}")
	public List<StrategyItemDto> getAllStrategyItemsByStrategyid(@PathVariable("strategyId") Integer strategyId) {
		List<StrategyItem> strategyItems = strategyItemService.getStrategyItemsByStrategy(strategyId);
		List<StrategyItemDto> strategyItemsDto = strategyItems.stream()
				.map(strategyItem -> convertStrategyItem(strategyItem)).collect(Collectors.toList());
		return strategyItemsDto;
	}

	
	/** 
	 * @param strategyItem
	 * @return StrategyItemDto
	 */
	private StrategyItemDto convertStrategyItem(StrategyItem strategyItem) {
		StrategyDto strategy = new StrategyDto(strategyItem.getStrategy().getStrategyId(),
				new StatusDto(strategyItem.getStrategy().getStatus().getStatusId(),
						strategyItem.getStrategy().getStatus().getValidityPeriod()),
				strategyItem.getStrategy().getStrategyName(), strategyItem.getStrategy().getTimeFrameStart(),
				strategyItem.getStrategy().getTimeFrameEnd(),
				new EnvironmentDto(strategyItem.getStrategy().getEnvironment().getEnvironmentId(),
						strategyItem.getStrategy().getEnvironment().getEnvironmentName()));
		return new StrategyItemDto(strategyItem.getItemId(), strategy, strategyItem.getStrategyItemName(),
				strategyItem.getDescription());
	}
}
