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
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityItemDto;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.model.dto.StrategyDto;
import com.bavostepbros.leap.domain.model.dto.StrategyItemDto;
import com.bavostepbros.leap.domain.service.capabilityitemservice.CapabilityItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/capabilityitem/")
public class CapabilityItemController {

	@Autowired
	private CapabilityItemService capabilityItemService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityItemDto addCapabilityItem(@ModelAttribute("capabilityId") Integer capabilityId,
			@ModelAttribute("itemId") Integer itemId,
			@ModelAttribute("strategicImportance") String strategicImportance) {

		CapabilityItem capabilityItem = capabilityItemService.save(capabilityId, itemId, strategicImportance);
		return convertCapabilityItem(capabilityItem);
	}

	@GetMapping("{capabilityId}/{itemId}")
	public CapabilityItemDto getCapabilityItem(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("itemId") Integer itemId) {

		CapabilityItem capabilityItem = capabilityItemService.get(capabilityId, itemId);
		return convertCapabilityItem(capabilityItem);
	}

	@PutMapping(path = "{capabilityId}/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public CapabilityItemDto updateCapabilityItem(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("itemId") Integer itemId, @ModelAttribute("strategicImportance") String strategicImportance) {

		CapabilityItem capabilityItem = capabilityItemService.save(capabilityId, itemId, strategicImportance);
		return convertCapabilityItem(capabilityItem);
	}

	@DeleteMapping(path = "{capabilityId}/{itemId}")
	public void deleteCapability(@PathVariable("capabilityId") Integer capabilityId,
			@PathVariable("itemId") Integer itemId) {
		capabilityItemService.delete(capabilityId, itemId);
	}

	@GetMapping(path = "all-capabilityitems-by-strategyitemid/{itemId}")
	public List<CapabilityItemDto> getAllCapabilityItemsByStrategyId(@PathVariable("itemId") Integer itemId) {

		List<CapabilityItem> capabilityItems = capabilityItemService.getCapabilityItemsByStrategyItem(itemId);
		List<CapabilityItemDto> capabilityItemsDto = capabilityItems.stream()
				.map(capabilityItem -> convertCapabilityItem(capabilityItem))
				.collect(Collectors.toList());
		return capabilityItemsDto;
	}

	@GetMapping(path = "all-capabilityitems-by-capabilityid/{capabilityId}")
	public List<CapabilityItemDto> getAllCapabilityItemsByCapabilityId(
			@PathVariable("capabilityId") Integer capabilityId) {

		List<CapabilityItem> capabilityItems = capabilityItemService.getCapabilityItemsByCapability(capabilityId);
		List<CapabilityItemDto> capabilityItemsDto = capabilityItems.stream()
				.map(capabilityItem -> convertCapabilityItem(capabilityItem))
				.collect(Collectors.toList());
		return capabilityItemsDto;
	}

	private CapabilityItemDto convertCapabilityItem(CapabilityItem capabilityItem) {
		CapabilityDto capabilityDto = new CapabilityDto(capabilityItem.getCapability().getCapabilityId(),
				new EnvironmentDto(capabilityItem.getCapability().getEnvironment().getEnvironmentId(),
						capabilityItem.getCapability().getEnvironment().getEnvironmentName()),
				new StatusDto(capabilityItem.getCapability().getStatus().getStatusId(),
						capabilityItem.getCapability().getStatus().getValidityPeriod()),
				capabilityItem.getCapability().getParentCapabilityId(),
				capabilityItem.getCapability().getCapabilityName(), capabilityItem.getCapability().getLevel(),
				capabilityItem.getCapability().isPaceOfChange(),
				capabilityItem.getCapability().getTargetOperatingModel(),
				capabilityItem.getCapability().getResourceQuality(),
				capabilityItem.getCapability().getInformationQuality(),
				capabilityItem.getCapability().getApplicationFit());

		StrategyItemDto strategyItemDto = new StrategyItemDto(capabilityItem.getStrategyItem().getItemId(),
				new StrategyDto(capabilityItem.getStrategyItem().getStrategy().getStrategyId(),
						new StatusDto(capabilityItem.getStrategyItem().getStrategy().getStatus().getStatusId(),
								capabilityItem.getStrategyItem().getStrategy().getStatus().getValidityPeriod()),
						capabilityItem.getStrategyItem().getStrategy().getStrategyName(),
						capabilityItem.getStrategyItem().getStrategy().getTimeFrameStart(),
						capabilityItem.getStrategyItem().getStrategy().getTimeFrameEnd(),
						new EnvironmentDto(
								capabilityItem.getStrategyItem().getStrategy().getEnvironment().getEnvironmentId(),
								capabilityItem.getStrategyItem().getStrategy().getEnvironment().getEnvironmentName())),
				capabilityItem.getStrategyItem().getStrategyItemName(),
				capabilityItem.getStrategyItem().getDescription());

		return new CapabilityItemDto(capabilityDto, strategyItemDto, capabilityItem.getStrategicImportance());
	}
}
