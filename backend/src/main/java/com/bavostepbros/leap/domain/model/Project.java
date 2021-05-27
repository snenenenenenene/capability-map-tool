package com.bavostepbros.leap.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class Project {
	
	@Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "PROJECTID")
	private Integer projectId;
	
	@NotBlank
	@Column(name = "PROJECTNAME", unique = true)
	private String projectName;
	
	@ManyToOne
	@JoinColumn(name = "PROGRAMID")
	private Program program;
	
	@OneToOne
	@JoinColumn(name = "STATUSID")
	private Status status;

	public Project(Integer projectId, String projectName, Program program, Status status) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.program = program;
		this.status = status;
	}

	public Project(String projectName, Program program, Status status) {
		this.projectName = projectName;
		this.program = program;
		this.status = status;
	}
	
}