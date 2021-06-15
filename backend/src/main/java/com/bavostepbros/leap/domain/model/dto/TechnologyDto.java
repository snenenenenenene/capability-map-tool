package com.bavostepbros.leap.domain.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** 
 * @return Integer
 */

/** 
 * @return String
 */

/** 
 * @return List<ITApplicationDto>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDto {
	private Integer technologyId;
	private String technologyName;
	private List<ITApplicationDto> itApplications;
	
	public TechnologyDto(Integer technologyId, String technologyName) {
		this.technologyId = technologyId;
		this.technologyName = technologyName;
	}
	
	
}
