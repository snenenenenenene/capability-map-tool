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

import com.bavostepbros.leap.domain.model.CapabilityItem;
import com.bavostepbros.leap.domain.model.dto.CapabilityItemDto;
import com.bavostepbros.leap.domain.service.capabilityitemservice.CapabilityItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/capabilityitem/")
public class CapabilityItemController {
	
	@Autowired
	private CapabilityItemService capabilityItemService;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityItemDto addCapabilityItem(
			@ModelAttribute("capabilityId") Integer capabilityId,
			@ModelAttribute("itemId") Integer itemId,
			@ModelAttribute("strategicImportance") String strategicImportance) {
		
		CapabilityItem capabilityItem = capabilityItemService.save(capabilityId, itemId, 
				strategicImportance);
		return new CapabilityItemDto(capabilityItem.getCapability(), 
				capabilityItem.getStrategyItem(), capabilityItem.getStrategicImportance());
	}
	
	@GetMapping("{capabilityId}/{itemId}")
	public CapabilityItemDto getCapabilityItem(
			@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("itemId") Integer itemId) {
		
		CapabilityItem capabilityItem = capabilityItemService.get(capabilityId, itemId);
		return new CapabilityItemDto(capabilityItem.getCapability(), 
				capabilityItem.getStrategyItem(), capabilityItem.getStrategicImportance());
	}
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityItemDto updateCapabilityItem(
			@ModelAttribute("capabilityId") Integer capabilityId,
			@ModelAttribute("itemId") Integer itemId,
			@ModelAttribute("strategicImportance") String strategicImportance) {
		
		CapabilityItem capabilityItem = capabilityItemService.save(capabilityId, itemId, 
				strategicImportance);
		return new CapabilityItemDto(capabilityItem.getCapability(), 
				capabilityItem.getStrategyItem(), capabilityItem.getStrategicImportance());
	}
	
	@DeleteMapping(path = "{capabilityId}/{itemId}")
	public void deleteCapability(@PathVariable("capabilityId") Integer capabilityId, 
			@PathVariable("itemId") Integer itemId) {
		capabilityItemService.delete(capabilityId, itemId);
	}
	
	@GetMapping(path = "all-capabilityitems-by-strategyitemid/{itemId}")
	public List<CapabilityItemDto> getAllCapabilityItemsByStrategyId(
			@PathVariable("itemId") Integer itemId) {
		
		List<CapabilityItem> capabilityItems = capabilityItemService.getCapabilityItemsByStrategyItem(itemId);
		List<CapabilityItemDto> capabilityItemsDto = capabilityItems.stream()
				.map(capabilityItem -> new CapabilityItemDto(capabilityItem.getCapability(), 
						capabilityItem.getStrategyItem(), capabilityItem.getStrategicImportance()))
				.collect(Collectors.toList());
		return capabilityItemsDto; 
	}
}
