package com.bavostepbros.leap.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.bavostepbros.leap.LeapApplication;
import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityApplication;
import com.bavostepbros.leap.domain.model.CapabilityInformation;
import com.bavostepbros.leap.domain.model.CapabilityItem;

import com.bavostepbros.leap.domain.model.dto.capabilitymap.CapabilityMapDto;
import com.bavostepbros.leap.domain.model.dto.capabilitymap.CapabilityMapItemDto;

import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.model.Information;
import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Resource;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.domain.model.dto.BusinessProcessDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityApplicationDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityInformationDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityItemDto;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.model.dto.ITApplicationDto;
import com.bavostepbros.leap.domain.model.dto.InformationDto;
import com.bavostepbros.leap.domain.model.dto.ProgramDto;
import com.bavostepbros.leap.domain.model.dto.ProjectDto;
import com.bavostepbros.leap.domain.model.dto.ResourceDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.model.dto.StrategyDto;
import com.bavostepbros.leap.domain.model.dto.StrategyItemDto;
import com.bavostepbros.leap.domain.model.dto.TechnologyDto;


/**
 *
 * @author Bavo Van Meel
 *
 */
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/environment/")
public class EnvironmentController {

	//TODO fix constructor injection
	@Autowired
	private EnvironmentService envService;

	@Autowired
	private ResourceLoader resourceLoader;

	/**
	 * @param environmentName
	 * @return EnvironmentDto
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EnvironmentDto addEnvironment(@ModelAttribute("environmentName") @Valid String environmentName) {
		Environment environment = envService.save(environmentName);
		return convertEnvironment(environment);
	}

	/**
	 * @param environmentId
	 * @return EnvironmentDto
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "{environmentId}")
	public EnvironmentDto getEnvironmentById(@PathVariable("environmentId") Integer environmentId) {
		Environment environment = envService.get(environmentId);
		return convertEnvironment(environment);
	}

	/**
	 * @param environmentName
	 * @return EnvironmentDto
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "environmentname/{environmentname}")
	public EnvironmentDto getEnvironmentByEnvironmentName(@PathVariable("environmentname") String environmentName) {
		Environment environment = envService.getByEnvironmentName(environmentName);
		return convertEnvironment(environment);
	}
	
	/**
	 * @param environmentId
	 * @return boolean
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "exists-by-id/{environmentId}")
	public boolean doesEnvironmentExistsById(@ModelAttribute("environmentId") Integer environmentId) {
		return envService.existsById(environmentId);
	}

	/**
	 * @param environmentName
	 * @return boolean
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "exists-by-environmentname/{environmentname}")
	public boolean doesEnvironmentNameExists(@PathVariable("environmentname") String environmentName) {
		return envService.existsByEnvironmentName(environmentName);
	}

	/**
	 * @return List<EnvironmentDto>
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping
	public List<EnvironmentDto> getAllEnvironments() {
		List<Environment> environments = envService.getAll();
		List<EnvironmentDto> environmentsDto = environments.stream().map(environment -> convertEnvironment(environment))
				.collect(Collectors.toList());
		return environmentsDto;
	}

	/**
	 * @param @PathVariable("environmentId"
	 * @return EnvironmentDto
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PutMapping(path = "{environmentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public EnvironmentDto updateEnvironment(@PathVariable("environmentId") Integer environmentId,
			@ModelAttribute("environmentName") String environmentName) {
		Environment environment = envService.update(environmentId, environmentName);
		return convertEnvironment(environment);
	}

	/**
	 * @param environmentId
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@DeleteMapping(path = "{environmentId}")
	public void deleteEnvironment(@PathVariable("environmentId") Integer environmentId) {
		envService.delete(environmentId);
	}

	// TODO fix exception catch

	/**
	 * @param environmentId
	 * @param level
	 * @return CapabilityMapDto
	 */
	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@GetMapping(path = "capabilitymap/{environmentId}")
	public CapabilityMapDto getCapabilityMap(@PathVariable("environmentId") Integer environmentId,
			@PathVariable("level") Integer level) {
		try {
			return constructMap(envService.get(environmentId), level);
		} catch (Exception e) {
			return new CapabilityMapDto();
		}
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER')")
	@PostMapping(path = "template/{templateName}")
	public String getTemplate(
			@PathVariable("templateName") String templateName) {
		try {
			String templateDir = "../../../capability-map-templates/";
			File file = new ClassPathResource(templateDir + templateName + ".json", LeapApplication.class).getFile();
			return new String(Files.readAllBytes(file.toPath()));
		} catch (FileNotFoundException e) {
			//TODO fix catches
			System.out.println("sioepke");
		} catch (IOException e) {
			System.out.println("sebonki" + e.getMessage());
		}
		return null;
	}


	private EnvironmentDto convertEnvironment(Environment environment) {
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
	}


	/**
	 * @param environment
	 * @return CapabilityMapDto
	 */
	private CapabilityMapDto constructMap(Environment environment, Integer level) {
		List<StrategyDto> strategiesDto = new ArrayList<StrategyDto>();
		if (environment.getStrategies() != null) {
			strategiesDto = environment.getStrategies().stream().map(strategy -> convertStrategy(strategy))
					.collect(Collectors.toList());
		}

		return new CapabilityMapDto(environment.getEnvironmentId(), environment.getEnvironmentName(),
				environment.getCapabilities().stream()
						.filter(i -> i.getParentCapabilityId().equals(0) || i.getParentCapabilityId() > level)
						.map(i -> constructCapabilityTree(i, environment.getCapabilities()))
						.collect(Collectors.toList()),
				strategiesDto);
	}


	/**
	 * @param environment
	 * @return EnvironmentDto
	 */
	private EnvironmentDto convertBasicEnvironment(Environment environment) {
		return new EnvironmentDto(environment.getEnvironmentId(), environment.getEnvironmentName());
	}


	/**
	 * @param status
	 * @return StatusDto
	 */
	private StatusDto convertBasicStatus(Status status) {
		return new StatusDto(status.getStatusId(), status.getValidityPeriod());
	}


	/**
	 * @param strategy
	 * @return StrategyDto
	 */
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


	/**
	 * @param strategyItem
	 * @return StrategyItemDto
	 */
	private StrategyItemDto convertStrategyItem(StrategyItem strategyItem) {
		return new StrategyItemDto(strategyItem.getItemId(), strategyItem.getStrategyItemName(),
				strategyItem.getDescription());
	}


	/**
	 * @param capabilityItem
	 * @return CapabilityItemDto
	 */
	private CapabilityItemDto convertCapabilityItem(CapabilityItem capabilityItem) {
		return new CapabilityItemDto(convertStrategyItem(capabilityItem.getStrategyItem()),
				capabilityItem.getStrategicImportance());
	}


	/**
	 * @param program
	 * @return ProgramDto
	 */
	private ProgramDto convertProgram(Program program) {
		return new ProgramDto(program.getProgramId(), program.getProgramName());
	}


	/**
	 * @param project
	 * @return ProjectDto
	 */
	private ProjectDto convertProject(Project project) {
		return new ProjectDto(project.getProjectId(), project.getProjectName(), convertProgram(project.getProgram()),
				convertBasicStatus(project.getStatus()));
	}


	/**
	 * @param businessProcess
	 * @return BusinessProcessDto
	 */
	private BusinessProcessDto convertBusinessProcess(BusinessProcess businessProcess) {
		return new BusinessProcessDto(businessProcess.getBusinessProcessId(), businessProcess.getBusinessProcessName(),
				businessProcess.getBusinessProcessDescription());
	}


	/**
	 * @param information
	 * @return InformationDto
	 */
	private InformationDto convertInformation(Information information) {
		return new InformationDto(information.getInformationId(), information.getInformationName(),
				information.getInformationDescription());
	}


	/**
	 * @param capabilityInformation
	 * @return CapabilityInformationDto
	 */
	private CapabilityInformationDto convertCapabilityInformation(CapabilityInformation capabilityInformation) {
		return new CapabilityInformationDto(convertInformation(capabilityInformation.getInformation()),
				capabilityInformation.getCriticality());
	}
	

	/**
	 * @param resource
	 * @return ResourceDto
	 */
	private ResourceDto convertResource(Resource resource) {
		return new ResourceDto(resource.getResourceId(), resource.getResourceName(), resource.getResourceDescription(), 
				resource.getFullTimeEquivalentYearlyValue());
	}
	

	/**
	 * @param technology
	 * @return TechnologyDto
	 */
	private TechnologyDto convertTechnology(Technology technology) {
		return new TechnologyDto(technology.getTechnologyId(), technology.getTechnologyName());
	}
	

	/**
	 * @param itApplication
	 * @return ITApplicationDto
	 */
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
	

	/**
	 * @param capabilityApplication
	 * @return CapabilityApplicationDto
	 */
	private CapabilityApplicationDto convertCapabilityApplication(CapabilityApplication capabilityApplication) {
		return new CapabilityApplicationDto(convertItApplication(capabilityApplication.getApplication()),
				capabilityApplication.getImportance(), capabilityApplication.getEfficiencySupport(),
				capabilityApplication.getFunctionalCoverage(), capabilityApplication.getCorrectnessBusinessFit(),
				capabilityApplication.getFuturePotential(), capabilityApplication.getCompleteness(),
				capabilityApplication.getCorrectnessInformationFit(), capabilityApplication.getAvailability());
	}


	/**
	 * @param capability
	 * @param pool
	 * @return CapabilityMapItemDto
	 */
	private CapabilityMapItemDto constructCapabilityTree(Capability capability, List<Capability> pool) {
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

		return new CapabilityMapItemDto(capability.getCapabilityId(), capability.getCapabilityName(), capability.getCapabilityDescription(),
				capability.getLevel(), capability.getPaceOfChange(), capability.getTargetOperatingModel(),
				capability.getResourceQuality(), capability.getInformationQuality(), capability.getApplicationFit(),
				convertBasicStatus(capability.getStatus()),
				pool.stream().filter(i -> i.getParentCapabilityId().equals(capability.getCapabilityId()))
						.map(i -> constructCapabilityTree(i, pool)).collect(Collectors.toList()),
				capabilityItemsDto, projectsDto, businessProcessDto, capabilityInformationDto, resourceDto,
				capabilityApplicationDto);
	}
}
