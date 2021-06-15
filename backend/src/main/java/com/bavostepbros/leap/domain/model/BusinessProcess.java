package com.bavostepbros.leap.domain.model;

import java.util.HashSet;
import java.util.Set;

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
	
	@NotBlank(message = "Businessprocess name is required.")
	@Column(name = "BUSINESSPROCESSNAME", unique = true)
	private String businessProcessName;
	
	@NotBlank(message = "Businessprocess description is required.")
	@Column(name = "BUSINESSPROCESSDESCRIPTION")
	private String businessProcessDescription;
	
	@ManyToMany(mappedBy = "businessProcess")
	private Set<Capability> capabilities = new HashSet<>();

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
	
	public void addCapability(Capability capability) {
		capabilities.add(capability);
		capability.getBusinessProcess().add(this);
		return;
	}
	
	public void removeCapability(Capability capability) {
		capabilities.add(capability);
		capability.getBusinessProcess().remove(this);
		return;
	}
	
	public Set<Capability> getCapabilities() {
		return capabilities;
	}
	
}
