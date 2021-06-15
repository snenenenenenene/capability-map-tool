package com.bavostepbros.leap.controller;

import java.util.ArrayList;
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

import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.domain.model.dto.ITApplicationDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.model.dto.TechnologyDto;
import com.bavostepbros.leap.domain.service.technologyservice.TechnologyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/technology/")
public class TechnologyController {

	@Autowired
	private TechnologyService technologyService;

	
	/** 
	 * @param technologyName
	 * @return TechnologyDto
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public TechnologyDto addTechnology(@ModelAttribute("technologyName") String technologyName) {
		Technology technology = technologyService.save(technologyName);
		return convertTechnology(technology);
	}

	
	/** 
	 * @param technologyId
	 * @return TechnologyDto
	 */
	@GetMapping(path = "{technologyId}")
	public TechnologyDto getTechnology(@PathVariable("technologyId") Integer technologyId) {
		Technology technology = technologyService.get(technologyId);
		return convertTechnology(technology);
	}

	
	/** 
	 * @param @PathVariable("technologyId"
	 * @return TechnologyDto
	 */
	@PutMapping(path = "{technologyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public TechnologyDto updateTechnology(@PathVariable("technologyId") Integer technologyId,
			@ModelAttribute("technologyName") String technologyName) {
		Technology technology = technologyService.update(technologyId, technologyName);
		return convertTechnology(technology);
	}

	
	/** 
	 * @param technologyId
	 */
	@DeleteMapping(path = "{technologyId}")
	public void deleteEnvironment(@PathVariable("technologyId") Integer technologyId) {
		technologyService.delete(technologyId);
	}

	
	/** 
	 * @return List<TechnologyDto>
	 */
	@GetMapping
	public List<TechnologyDto> getAllTechnologies() {
		List<Technology> technologies = technologyService.getAll();
		List<TechnologyDto> technologiesDto = technologies.stream()
				.map(technology -> convertTechnology(technology))
				.collect(Collectors.toList());
		return technologiesDto;
	}

	
	/** 
	 * @param technology
	 * @return TechnologyDto
	 */
	private TechnologyDto convertTechnology(Technology technology) {
		List<ITApplicationDto> itApplicationsDto = new ArrayList<ITApplicationDto>();
		if (technology.getItApplications() != null) {
			itApplicationsDto = technology.getItApplications().stream()
					.map(itApplication -> new ITApplicationDto(itApplication.getItApplicationId(),
							new StatusDto(itApplication.getStatus().getStatusId(),
									itApplication.getStatus().getValidityPeriod()),
							itApplication.getName(), itApplication.getVersion(), itApplication.getPurchaseDate(),
							itApplication.getEndOfLife(), itApplication.getCurrentScalability(),
							itApplication.getExpectedScalability(), itApplication.getCurrentPerformance(),
							itApplication.getExpectedPerformance(), itApplication.getCurrentSecurityLevel(),
							itApplication.getExpectedSecurityLevel(), itApplication.getCurrentStability(),
							itApplication.getExpectedStability(), itApplication.getCurrencyType(),
							itApplication.getCostCurrency(), itApplication.getCurrentValue(),
							itApplication.getCurrentYearlyCost(), itApplication.getAcceptedYearlyCost(),
							itApplication.getTimeValue()))
					.collect(Collectors.toList());
		}
		return new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName(), itApplicationsDto);
	}

}
