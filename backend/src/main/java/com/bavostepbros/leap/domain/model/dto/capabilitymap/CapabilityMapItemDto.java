package com.bavostepbros.leap.domain.model.dto.capabilitymap;

import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityMapItemDto {
	private Integer capabilityId;
    private String capabilityName;
    private CapabilityLevel level;
    private boolean paceOfChange;
    private String targetOperatingModel;
    private Integer resourceQuality;
    private Integer informationQuality;
    private Integer applicationFit;
    private Status status;
    private List<CapabilityMapItemDto> children;
}
