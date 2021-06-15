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
public class CapabilityApplicationId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "CAPABILITYID")

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */
	private Integer capabilityId;
	
	@Column(name = "APPLICATIONID")
    private Integer applicationId;
	
	public CapabilityApplicationId(Integer capabilityId, Integer applicationId) {
		this.capabilityId = capabilityId;
		this.applicationId = applicationId;
	}
	
	
	/** 
	 * @param o
	 * @return boolean
	 */
	@Override
    public boolean equals(Object o) {
		if (o == this)
            return true;
		if (!(o instanceof CapabilityApplicationId)) {
            return false;
        }
		CapabilityApplicationId capabilityApplicationId = (CapabilityApplicationId) o;
		return Objects.equals(capabilityId, capabilityApplicationId.capabilityId) &&
				Objects.equals(applicationId, capabilityApplicationId.applicationId);	
	}
	
	
	/** 
	 * @return int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(capabilityId, applicationId);
	}
}
