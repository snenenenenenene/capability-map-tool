package com.bavostepbros.leap.domain.model.dto;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Information;
import com.bavostepbros.leap.domain.model.strategicimportance.StrategicImportance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityInformationDto {
	private Capability capability;
	private Information information;
	private StrategicImportance criticality;
}
