package com.bavostepbros.leap.domain.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class CapabilityInformationId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "CAPABILITYID")
	private Integer capabilityId;
	
	@Column(name = "INFORMATIONID")
    private Integer informationId;

	public CapabilityInformationId(Integer capabilityId, Integer informationId) {
		this.capabilityId = capabilityId;
		this.informationId = informationId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof CapabilityInformationId)) {
			return false;
		}
		CapabilityInformationId capabilityInformationId = (CapabilityInformationId) o;
		return Objects.equals(capabilityId, capabilityInformationId.capabilityId) &&
				Objects.equals(informationId, capabilityInformationId.informationId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(capabilityId, informationId);
	}
}
