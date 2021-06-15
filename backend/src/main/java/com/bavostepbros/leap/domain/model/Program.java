package com.bavostepbros.leap.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

/** 
 * @return Integer
 */

/** 
 * @return String
 */

/** 
 * @return List<Project>
 */
@Getter
@Setter
@NoArgsConstructor

/** 
 * @return boolean
 */

/** 
 * @return boolean
 */

/** 
 * @return int
 */
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
	
	@OneToMany(mappedBy = "program")
	private List<Project> projects;

	public Program(Integer programId, String programName) {
		this.programId = programId;
		this.programName = programName;
	}

	public Program(String programName) {
		this.programName = programName;
	}
	
}
