package com.bavostepbros.leap.domain.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"PROJECTID", "PROGRAMID", "STATUSID"})})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project {
	
	@Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "PROJECTID")
	private Integer projectId;

/** 
 * @return Integer
 */

/** 
 * @return String
 */

/** 
 * @return Program
 */

/** 
 * @return Status
 */
	
	@NotBlank(message = "Project name is required.")
	@Column(name = "PROJECTNAME", unique = true)
	private String projectName;

/** 
 * @return boolean
 */

/** 
 * @return boolean
 */

/** 
 * @return int
 */
	
	@ManyToOne
	@JoinColumn(name = "PROGRAMID")
	private Program program;
	
	@OneToOne
	@JoinColumn(name = "STATUSID")
	private Status status;
	
	@ManyToMany(mappedBy = "projects")
	private Set<Capability> capabilities;
	
	public Project(String projectName, Program program, Status status) {
		this.projectName = projectName;
		this.program = program;
		this.status = status;
	}

	public Project(Integer projectId, String projectName, Program program, Status status) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.program = program;
		this.status = status;
	}
	
	
	/** 
	 * @param capability
	 */
	public void addCapability(Capability capability) {
		capabilities.add(capability);
		capability.getProjects().add(this);
		return;
	}
	
	
	/** 
	 * @param capability
	 */
	public void removeCapability(Capability capability) {
		capabilities.remove(capability);
		capability.getProjects().remove(this);
		return;
	}
	
	
	/** 
	 * @return Set<Capability>
	 */
	public Set<Capability> getCapabilities() {
		return capabilities;
	}
	
}
