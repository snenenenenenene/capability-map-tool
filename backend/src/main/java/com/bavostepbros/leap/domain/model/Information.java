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
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Information {

	@Id
	@GeneratedValue
	@EqualsAndHashCode.Include
	@Column(name = "INFORMATIONID")
	private Integer informationId;

	@NotBlank(message = "Capability name is required.")
	@Column(name = "INFORMATIONNAME", unique = true)
	private String informationName;
	
	@NotBlank(message = "Capability description is required.")
	@Column(name = "INFORMATIONDESCRIPTION")
	private String informationDescription;

	@OneToMany(mappedBy = "information")
	private List<CapabilityInformation> capabilityInformation;

	public Information(Integer informationId, @NotBlank String informationName, String informationDescription) {
		this.informationId = informationId;
		this.informationName = informationName;
		this.informationDescription = informationDescription;
	}

	public Information(@NotBlank String informationName, String informationDescription) {
		this.informationName = informationName;
		this.informationDescription = informationDescription;
	}

}
