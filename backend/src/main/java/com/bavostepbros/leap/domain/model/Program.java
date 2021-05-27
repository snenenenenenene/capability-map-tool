package com.bavostepbros.leap.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Program {
	
	@Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "PROGRAMID")
	private Integer programId;
	
	@NotBlank(message = "Program name is required")
	@Column(name = "PROGRAMNAME", unique = true)
    private String programName;

	public Program(Integer programId, String programName) {
		super();
		this.programId = programId;
		this.programName = programName;
	}

	public Program(String programName) {
		super();
		this.programName = programName;
	}
	
}
