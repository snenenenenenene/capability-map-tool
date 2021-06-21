package com.bavostepbros.leap.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Resource {
	
	@Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "RESOURCEID")
	private Integer resourceId;
	
	@NotBlank(message = "Resource name is required.")
	@Column(name = "RESOURCENAME", unique = true)
	private String resourceName;
	
	@NotBlank(message = "Resource descirption is required.")
	@Column(name = "RESOURCEDESCRIPTION")
	private String resourceDescription;
	
	@NotNull(message = "FTE yearly equivalent must not be null.")
	@Column(name = "FULLTIMEEQUIVALENTYEARLYVALUE")
	private Double fullTimeEquivalentYearlyValue;
	
	@ManyToMany(mappedBy = "resources")
	private Set<Capability> capabilities = new HashSet<>();

	public Resource(Integer resourceId, String resourceName, String resourceDescription,
			Double fullTimeEquivalentYearlyValue) {
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.resourceDescription = resourceDescription;
		this.fullTimeEquivalentYearlyValue = fullTimeEquivalentYearlyValue;
	}

	public Resource(String resourceName, String resourceDescription,
			Double fullTimeEquivalentYearlyValue) {
		this.resourceName = resourceName;
		this.resourceDescription = resourceDescription;
		this.fullTimeEquivalentYearlyValue = fullTimeEquivalentYearlyValue;
	}
	
	public void addCapability(Capability capability) {
		capabilities.add(capability);
		capability.getResources().add(this);
		return;
	}
	
	public void removeCapability(Capability capability) {
		capabilities.remove(capability);
		capability.getResources().remove(this);
		return;
	}
	
	public Set<Capability> getCapabilities() {
		return capabilities;
	}
	
}
