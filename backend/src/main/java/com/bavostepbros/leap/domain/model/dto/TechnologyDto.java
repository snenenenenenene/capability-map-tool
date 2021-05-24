package com.bavostepbros.leap.domain.model.dto;

import java.util.List;

import com.bavostepbros.leap.domain.model.ITApplication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDto {
	private Integer technologyId;
	private String technologyName;
	private List<ITApplication> itApplications;
}
