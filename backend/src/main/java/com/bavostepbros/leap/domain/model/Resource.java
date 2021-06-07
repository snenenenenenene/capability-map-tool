package com.bavostepbros.leap.domain.model;

import java.util.List;

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
	private List<Capability> capabilities;

	public Resource(Integer resourceId, @NotBlank String resourceName, @NotBlank String resourceDescription,
			Double fullTimeEquivalentYearlyValue) {
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.resourceDescription = resourceDescription;
		this.fullTimeEquivalentYearlyValue = fullTimeEquivalentYearlyValue;
	}

	public Resource(@NotBlank String resourceName, @NotBlank String resourceDescription,
			Double fullTimeEquivalentYearlyValue) {
		this.resourceName = resourceName;
		this.resourceDescription = resourceDescription;
		this.fullTimeEquivalentYearlyValue = fullTimeEquivalentYearlyValue;
	}
	
}
