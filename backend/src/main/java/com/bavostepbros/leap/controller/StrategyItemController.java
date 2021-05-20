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
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StrategyItemDto addStrategyItem(
			@ModelAttribute("strategyId") Integer strategyId,
			@ModelAttribute("strategyItemName") String strategyItemName,
			@ModelAttribute("description") String description) {
		
		StrategyItem strategyItem = strategyItemService.save(strategyId, 
				strategyItemName, description);
		return new StrategyItemDto(strategyItem.getItemId(), 
				strategyItem.getStrategy(), strategyItem.getStrategyItemName(),
				strategyItem.getDescription());
	}
	
	@GetMapping("{itemId}")
	public StrategyItemDto getStrategyItemByItemid(
			@PathVariable("itemId") Integer itemId) {
		
		StrategyItem strategyItem = strategyItemService.get(itemId);
		return new StrategyItemDto(strategyItem.getItemId(), strategyItem.getStrategy(), 
				strategyItem.getStrategyItemName(), strategyItem.getDescription());
	}
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public StrategyItemDto updateStrategyItem(
			@ModelAttribute("itemId") Integer itemId,
			@ModelAttribute("strategyId") Integer strategyId,
			@ModelAttribute("strategyItemName") String strategyItemName,
			@ModelAttribute("description") String description) {
		
		StrategyItem strategyItem = strategyItemService.update(itemId, strategyId, 
				strategyItemName, description);
		return new StrategyItemDto(strategyItem.getItemId(), strategyItem.getStrategy(), 
				strategyItem.getStrategyItemName(), strategyItem.getDescription());
	}
	
	@DeleteMapping(path = "{itemId}")
	public void deleteStrategyItem(
			@PathVariable("itemId") Integer itemId) {
		strategyItemService.delete(itemId);
	}
	
	@GetMapping
	public List<StrategyItemDto> getAllStrategyItems() {
		List<StrategyItem> strategyItems = strategyItemService.getAll();
		List<StrategyItemDto> strategyItemsDto = strategyItems.stream()
				.map(strategyItem -> new StrategyItemDto(strategyItem.getItemId(), 
						strategyItem.getStrategy(), strategyItem.getStrategyItemName(), 
						strategyItem.getDescription()))
				.collect(Collectors.toList());
		return strategyItemsDto;
	}
	
	@GetMapping(path = "exists-by-id/{itemId}")
	public boolean doesStrategyItemExistsById(
			@PathVariable("itemId") Integer itemId) {
		return strategyItemService.existsById(itemId);
	}
	
	@GetMapping(path = "exists-by-strategyitemname/{strategyItemName}")
	public boolean doesStrategyItemExistsByStrategyitemname(
			@PathVariable("strategyItemName") String strategyItemName) {
		return strategyItemService.existsByStrategyItemName(strategyItemName);
	}
	
	@GetMapping(path = "strategyitemname/{strategyItemName}") 
	public StrategyItemDto getStrategyByStrategyName(
			@ModelAttribute("strategyItemName") String strategyItemName) {
		StrategyItem strategyItem = strategyItemService.getStrategyItemByStrategyItemName(strategyItemName);
		return new StrategyItemDto(strategyItem.getItemId(), strategyItem.getStrategy(), 
				strategyItem.getStrategyItemName(), strategyItem.getDescription());
	}
	
	@GetMapping(path = "all-strategyitems-by-strategyid/{strategyId}")
	public List<StrategyItemDto> getAllStrategyItemsByStrategyid(
			@PathVariable("strategyId") Integer strategyId) {
		List<StrategyItem> strategyItems = strategyItemService.getStrategyItemsByStrategy(strategyId);
		List<StrategyItemDto> strategyItemsDto = strategyItems.stream()
				.map(strategyItem -> new StrategyItemDto(strategyItem.getItemId(), 
						strategyItem.getStrategy(), strategyItem.getStrategyItemName(), 
						strategyItem.getDescription()))
				.collect(Collectors.toList());
		return strategyItemsDto;
	}
}
