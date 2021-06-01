package com.bavostepbros.leap.domain.model.dto.capabilitymap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.bavostepbros.leap.domain.model.dto.StrategyDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityMapDto {
	private Integer environmentId;
    private String environmentName;
    private List<CapabilityMapItemDto> capabilities;
    private List<StrategyDto> strategies;
}
