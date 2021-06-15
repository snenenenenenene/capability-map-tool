package com.bavostepbros.leap.domain.model.dto.capabilitymap;

import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.model.dto.BusinessProcessDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityApplicationDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityInformationDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityItemDto;
import com.bavostepbros.leap.domain.model.dto.ProjectDto;
import com.bavostepbros.leap.domain.model.dto.ResourceDto;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/** 
 * @return Integer
 */

/** 
 * @return String
 */

/** 
 * @return String
 */

/** 
 * @return CapabilityLevel
 */

/** 
 * @return PaceOfChange
 */

/** 
 * @return TargetOperatingModel
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return StatusDto
 */

/** 
 * @return List<CapabilityMapItemDto>
 */

/** 
 * @return List<CapabilityItemDto>
 */

/** 
 * @return List<ProjectDto>
 */

/** 
 * @return List<BusinessProcessDto>
 */

/** 
 * @return List<CapabilityInformationDto>
 */

/** 
 * @return List<ResourceDto>
 */

/** 
 * @return List<CapabilityApplicationDto>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityMapItemDto {
	private Integer capabilityId;
    private String capabilityName;
    private String description;
    private CapabilityLevel level;
    private PaceOfChange paceOfChange;
    private TargetOperatingModel targetOperatingModel;
    private Integer resourceQuality;
    private Integer informationQuality;
    private Integer applicationFit;
    private StatusDto status;
    private List<CapabilityMapItemDto> children;
    private List<CapabilityItemDto> capabilityItems;
    private List<ProjectDto> projects;
    private List<BusinessProcessDto> businessprocess;
    private List<CapabilityInformationDto> capabilityInformation;
    private List<ResourceDto> resources;
    private List<CapabilityApplicationDto> capabilityApplications;

}
