package com.bavostepbros.leap.domain.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.bavostepbros.leap.domain.model.strategicimportance.StrategicImportance;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CapabilityInformation {

	@EmbeddedId
	private CapabilityInformationId capabilityInformationId;

	@ManyToOne
	@MapsId("capabilityId")
	@EqualsAndHashCode.Include
	private Capability capability;

	@ManyToOne
	@MapsId("informationId")
	@EqualsAndHashCode.Include
	private Information information;

	@Column(name = "CRITICALITY")
	private StrategicImportance criticality;

	public CapabilityInformation(Capability capability, Information information, StrategicImportance criticality) {
		this.capability = capability;
		this.information = information;
		this.criticality = criticality;
		this.capabilityInformationId = new CapabilityInformationId(capability.getCapabilityId(),
				information.getInformationId());
	}

}
