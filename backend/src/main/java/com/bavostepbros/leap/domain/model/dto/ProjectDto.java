package com.bavostepbros.leap.domain.model.dto;

import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.domain.model.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
	private Integer projectId;
	private Program program;
	private Status status;
}
