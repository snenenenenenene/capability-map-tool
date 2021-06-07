package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.dto.BusinessProcessDto;
import com.bavostepbros.leap.domain.service.businessprocessservice.BusinessProcessService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/businessprocess/")
public class BusinessProcessController {

	@Autowired
	private BusinessProcessService businessProcessService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BusinessProcessDto addBusinessProcess(
			@Valid @ModelAttribute("businessProcessName") String businessProcessName,
			@Valid @ModelAttribute("businessProcessDescription") String businessProcessDescription) {
		BusinessProcess businessProcess = businessProcessService.save(businessProcessName, businessProcessDescription);
		return convertBusinessProcess(businessProcess);
	}

	@GetMapping(path = "{businessProcessId}")
	public BusinessProcessDto getBusinessProcess(@PathVariable("businessProcessId") @Positive Integer businessProcessId) {
		BusinessProcess businessProcess = businessProcessService.get(businessProcessId);
		return convertBusinessProcess(businessProcess);
	}

	@PutMapping(path = "{businessProcessId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BusinessProcessDto updateBusinessProcess(@PathVariable("businessProcessId") @Positive Integer businessProcessId,
			@ModelAttribute("businessProcessName") @NotBlank String businessProcessName,
			@ModelAttribute("businessProcessDescription") @NotBlank String businessProcessDescription) {
		BusinessProcess businessProcess = businessProcessService.update(businessProcessId, businessProcessName,
				businessProcessDescription);
		return convertBusinessProcess(businessProcess);
	}

	@DeleteMapping(path = "{businessProcessId}")
	public void deleteBusinessProcess(@PathVariable("businessProcessId") @Positive Integer businessProcessId) {
		businessProcessService.delete(businessProcessId);
	}

	@GetMapping(path = "businessProcessName/{businessProcessName}")
	public BusinessProcessDto getBusinessProcess(
			@Valid @PathVariable("businessProcessName") String businessProcessName) {
		BusinessProcess businessProcess = businessProcessService.getBusinessProcessByName(businessProcessName);
		return convertBusinessProcess(businessProcess);
	}

	@GetMapping
	public List<BusinessProcessDto> getAllBusinessProcess() {
		List<BusinessProcess> businessProcessList = businessProcessService.getAll();
		List<BusinessProcessDto> businessProcessDto = businessProcessList.stream()
				.map(businessProcess -> convertBusinessProcess(businessProcess)).collect(Collectors.toList());
		return businessProcessDto;
	}

	private BusinessProcessDto convertBusinessProcess(BusinessProcess businessProcess) {
		return new BusinessProcessDto(businessProcess.getBusinessProcessId(), businessProcess.getBusinessProcessName(),
				businessProcess.getBusinessProcessDescription());
	}

}
