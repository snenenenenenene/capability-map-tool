package com.bavostepbros.leap.controller;

import com.bavostepbros.leap.domain.model.*;
import com.bavostepbros.leap.domain.model.dto.*;
import com.bavostepbros.leap.domain.model.dto.capabilitymap.CapabilityMapDto;
import com.bavostepbros.leap.domain.model.dto.capabilitymap.CapabilityMapItemDto;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Bavo Van Meel
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/environment/")
public class EnvironmentController {

	@Autowired
	private EnvironmentService envService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EnvironmentDto addEnvironment(@ModelAttribute("environmentName") String environmentName) {
		Environment environment = envService.save(environmentName);
		return convertEnvironment(environment);
	}

	@GetMapping(path = "{environmentId}")
	public EnvironmentDto getEnvironmentById(@PathVariable("environmentId") Integer environmentId) {
		Environment environment = envService.get(environmentId);
		return convertEnvironment(environment);
	}

	@GetMapping(path = "environmentname/{environmentname}")
	public EnvironmentDto getEnvironmentByEnvironmentName(@PathVariable("environmentname") String environmentName) {
		Environment environment = envService.getByEnvironmentName(environmentName);
		return convertEnvironment(environment);
	}

	@GetMapping(path = "exists-by-id/{environmentId}")
	public boolean doesEnvironmentExistsById(@ModelAttribute("environmentId") Integer environmentId) {
		return envService.existsById(environmentId);
	}

	@GetMapping(path = "exists-by-environmentname/{environmentname}")
	public boolean doesEnvironmentNameExists(@PathVariable("environmentname") String environmentName) {
		return envService.existsByEnvironmentName(environmentName);
	}

	@GetMapping
	public List<EnvironmentDto> getAllEnvironments() {
		List<Environment> environments = envService.getAll();
		List<EnvironmentDto> environmentsDto = environments.stream().map(environment -> convertEnvironment(environment))
				.collect(Collectors.toList());
		return environmentsDto;
	}

	@PutMapping(path = "{environmentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EnvironmentDto updateEnvironment(@PathVariable("environmentId") Integer environmentId,
			@ModelAttribute("environmentName") String environmentName) {
		Environment environment = envService.update(environmentId, environmentName);
		return convertEnvironment(environment);
	}

	@DeleteMapping(path = "{environmentId}")
	public void deleteEnvironment(@PathVariable("environmentId") Integer environmentId) {
		envService.delete(environmentId);
	}

	@GetMapping(path = "capabilitymap/{environmentId}")
	public CapabilityMapDto getCapabilityMap(@PathVariable("environmentId") Integer environmentId) {
		try {
			return constructMap(envService.get(environmentId));
		} catch (Exception e) {
			return new CapabilityMapDto();
		}
	}

	private EnvironmentDto convertEnvironment(Environment environment) {
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
	}

	private CapabilityMapDto constructMap(Environment environment) {
		List<StrategyDto> strategiesDto = new ArrayList<StrategyDto>();
		if (environment.getStrategies() != null) {
			strategiesDto = environment.getStrategies().stream().map(strategy -> convertStrategy(strategy))
					.collect(Collectors.toList());
		}

		return new CapabilityMapDto(environment.getEnvironmentId(), environment.getEnvironmentName(),
				environment.getCapabilities().stream().filter(i -> i.getParentCapabilityId().equals(0))
						.map(i -> constructGraph(i, environment.getCapabilities())).collect(Collectors.toList()),
				strategiesDto);
	}

	private EnvironmentDto convertBasicEnvironment(Environment environment) {
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
	}

	private StatusDto convertBasicStatus(Status status) {
		return new StatusDto(status.getStatusId(), status.getValidityPeriod());
	}

	private StrategyDto convertStrategy(Strategy strategy) {
		List<StrategyItemDto> strategyItemsDto = new ArrayList<StrategyItemDto>();
		if (strategy.getItems() != null) {
			strategyItemsDto = strategy.getItems().stream()
					.map(strategyItem -> convertStrategyItem(strategyItem))
					.collect(Collectors.toList());
		}
		return new StrategyDto(strategy.getStrategyId(), convertBasicStatus(strategy.getStatus()),
				strategy.getStrategyName(), strategy.getTimeFrameStart(), strategy.getTimeFrameEnd(),
				convertBasicEnvironment(strategy.getEnvironment()), strategyItemsDto);
	}

	private StrategyItemDto convertStrategyItem(StrategyItem strategyItem) {
		return new StrategyItemDto(strategyItem.getItemId(), strategyItem.getStrategyItemName(),
				strategyItem.getDescription());
	}

	private CapabilityItemDto convertCapabilityItem(CapabilityItem capabilityItem) {
		return new CapabilityItemDto(convertStrategyItem(capabilityItem.getStrategyItem()),
				capabilityItem.getStrategicImportance());
	}

	private ProgramDto convertProgram(Program program) {
		return new ProgramDto(program.getProgramId(), program.getProgramName());
	}

	private ProjectDto convertProject(Project project) {
		return new ProjectDto(project.getProjectId(), project.getProjectName(), convertProgram(project.getProgram()),
				convertBasicStatus(project.getStatus()));
	}

	private BusinessProcessDto convertBusinessProcess(BusinessProcess businessProcess) {
		return new BusinessProcessDto(businessProcess.getBusinessProcessId(), businessProcess.getBusinessProcessName(),
				businessProcess.getBusinessProcessDescription());
	}

	private InformationDto convertInformation(Information information) {
		return new InformationDto(information.getInformationId(), information.getInformationName(),
				information.getInformationDescription());
	}

	private CapabilityInformationDto convertCapabilityInformation(CapabilityInformation capabilityInformation) {
		return new CapabilityInformationDto(convertInformation(capabilityInformation.getInformation()),
				capabilityInformation.getCriticality());
	}
	
	private ResourceDto convertResource(Resource resource) {
		return new ResourceDto(resource.getResourceId(), resource.getResourceName(), resource.getResourceDescription(), 
				resource.getFullTimeEquivalentYearlyValue());
	}
	
	private TechnologyDto convertTechnology(Technology technology) {
		return new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName());
	}
	
	private ITApplicationDto convertItApplication(ITApplication itApplication) {
		List<TechnologyDto> technologiesDto = new ArrayList<TechnologyDto>();
		if (itApplication.getTechnologies() != null) {
			technologiesDto = itApplication.getTechnologies().stream()
					.map(technology -> convertTechnology(technology))
					.collect(Collectors.toList());
		}
		return new ITApplicationDto(itApplication.getItApplicationId(), convertBasicStatus(itApplication.getStatus()),
				itApplication.getName(), itApplication.getVersion(), itApplication.getPurchaseDate(),
				itApplication.getEndOfLife(), itApplication.getCurrentScalability(), 
				itApplication.getExpectedScalability(), itApplication.getCurrentPerformance(),
				itApplication.getExpectedPerformance(), itApplication.getCurrentSecurityLevel(),
				itApplication.getExpectedSecurityLevel(), itApplication.getCurrentStability(),
				itApplication.getExpectedStability(), itApplication.getCurrencyType(), itApplication.getCostCurrency(),
				itApplication.getCurrentValue(), itApplication.getCurrentYearlyCost(), 
				itApplication.getAcceptedYearlyCost(), itApplication.getTimeValue(), technologiesDto);
	}
	
	private CapabilityApplicationDto convertCapabilityApplication(CapabilityApplication capabilityApplication) {
		return new CapabilityApplicationDto(convertItApplication(capabilityApplication.getApplication()),
				capabilityApplication.getImportance(), capabilityApplication.getEfficiencySupport(),
				capabilityApplication.getFunctionalCoverage(), capabilityApplication.getCorrectnessBusinessFit(),
				capabilityApplication.getFuturePotential(), capabilityApplication.getCompleteness(),
				capabilityApplication.getCorrectnessInformationFit(), capabilityApplication.getAvailability());
	}

	private CapabilityMapItemDto constructGraph(Capability capability, List<Capability> pool) {
		List<CapabilityItemDto> capabilityItemsDto = new ArrayList<CapabilityItemDto>();
		if (capability.getCapabilityItems() != null) {
			capabilityItemsDto = capability.getCapabilityItems().stream()
					.map(capabilityItem -> convertCapabilityItem(capabilityItem))
					.collect(Collectors.toList());
		}

		List<ProjectDto> projectsDto = new ArrayList<ProjectDto>();
		if (capability.getProjects() != null) {
			projectsDto = capability.getProjects().stream()
					.map(project -> convertProject(project))
					.collect(Collectors.toList());
		}

		List<BusinessProcessDto> businessProcessDto = new ArrayList<BusinessProcessDto>();
		if (capability.getBusinessProcess() != null) {
			businessProcessDto = capability.getBusinessProcess().stream()
					.map(businessProcess -> convertBusinessProcess(businessProcess))
					.collect(Collectors.toList());
		}
		
		List<CapabilityInformationDto> capabilityInformationDto = new ArrayList<CapabilityInformationDto>();
		if (capability.getCapabilityInformation() != null) {
			capabilityInformationDto = capability.getCapabilityInformation().stream()
					.map(capabilityInformation -> convertCapabilityInformation(capabilityInformation))
					.collect(Collectors.toList());
		}
		
		List<ResourceDto> resourceDto = new ArrayList<ResourceDto>();
		if (capability.getResources() != null) {
			resourceDto = capability.getResources().stream()
					.map(resource -> convertResource(resource))
					.collect(Collectors.toList());
		}
		
		List<CapabilityApplicationDto> capabilityApplicationDto = new ArrayList<CapabilityApplicationDto>();
		if (capability.getCapabilityApplication() != null) {
			capabilityApplicationDto = capability.getCapabilityApplication().stream()
					.map(capabilityApplication -> convertCapabilityApplication(capabilityApplication))
					.collect(Collectors.toList());
		}

		return new CapabilityMapItemDto(capability.getCapabilityId(), capability.getCapabilityName(),
				capability.getLevel(), capability.getPaceOfChange(), capability.getTargetOperatingModel(),
				capability.getResourceQuality(), capability.getInformationQuality(), capability.getApplicationFit(),
				convertBasicStatus(capability.getStatus()),
				pool.stream().filter(i -> i.getParentCapabilityId().equals(capability.getCapabilityId()))
						.map(i -> constructGraph(i, pool)).collect(Collectors.toList()),
				capabilityItemsDto, projectsDto, businessProcessDto, capabilityInformationDto, resourceDto,
				capabilityApplicationDto);
	}
}
