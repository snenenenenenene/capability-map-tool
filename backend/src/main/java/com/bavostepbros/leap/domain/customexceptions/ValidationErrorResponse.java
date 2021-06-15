package com.bavostepbros.leap.domain.customexceptions;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** 
 * @return List<Violation>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
	private List<Violation> violations = new ArrayList<Violation>();
}
