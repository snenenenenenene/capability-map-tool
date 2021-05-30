package com.bavostepbros.leap.domain.model.dto.capabilitymap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityMapDto {
    private String environmentName;
    private List<CapabilityMapItemDto> capabilities;
}
