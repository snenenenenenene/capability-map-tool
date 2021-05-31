package com.bavostepbros.leap.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
public class BusinessProcess {
	
	@Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "BUSINESSPROCESSID")
	private Integer businessProcessId;
	
	@NotBlank
	@Column(name = "BUSINESSPROCESSNAME", unique = true)
	private String businessProcessName;
	
	@Column(name = "BUSINESSPROCESSDESCRIPTION")
	private String businessProcessDescription;
	
	@ManyToMany(mappedBy = "businessProcess")
	private List<Capability> capabilities;

	public BusinessProcess(@NotBlank String businessProcessName, String businessProcessDescription) {
		this.businessProcessName = businessProcessName;
		this.businessProcessDescription = businessProcessDescription;
	}

	public BusinessProcess(Integer businessProcessId, @NotBlank String businessProcessName,
			String businessProcessDescription) {
		this.businessProcessId = businessProcessId;
		this.businessProcessName = businessProcessName;
		this.businessProcessDescription = businessProcessDescription;
	}
	
}
