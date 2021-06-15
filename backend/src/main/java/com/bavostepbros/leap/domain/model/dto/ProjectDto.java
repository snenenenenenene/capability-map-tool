package com.bavostepbros.leap.domain.model.dto;

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
 * @return ProgramDto
 */

/** 
 * @return StatusDto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
	private Integer projectId;
	private String projectName;
	private ProgramDto program;
	private StatusDto status;
	
}
