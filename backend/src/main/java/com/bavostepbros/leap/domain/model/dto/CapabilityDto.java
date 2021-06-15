package com.bavostepbros.leap.domain.model.dto;

import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** 
 * @return Integer
 */

/** 
 * @return EnvironmentDto
 */

/** 
 * @return StatusDto
 */

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityDto {
	private Integer capabilityId;
	private EnvironmentDto environment;
	private StatusDto status;
	private Integer parentCapabilityId;
	private String capabilityName;
	private String capabilityDescription;
	private CapabilityLevel level;
	private PaceOfChange paceOfChange;
	private TargetOperatingModel targetOperatingModel;
	private Integer resourceQuality;
	private Integer informationQuality;
	private Integer applicationFit;
	
}
