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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnvironmentDto {
	private Integer environmentId;
	private String environmentName;
	
	public EnvironmentDto(Integer environmentId) {
		this.environmentId = environmentId;
	}
		
}
