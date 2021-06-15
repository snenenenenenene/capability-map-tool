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
 * @return String
 */

/** 
 * @return Double
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDto {
	private Integer resourceId;
	private String resourceName;
	private String resourceDescription;
	private Double fullTimeEquivalentYearlyValue;
}
