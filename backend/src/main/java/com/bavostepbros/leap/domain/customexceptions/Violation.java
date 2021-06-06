package com.bavostepbros.leap.domain.customexceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Violation {
	private String name;
	private String message;
}
