package com.bavostepbros.leap.domain.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
*
* @author Bavo Van Meel
*
*/
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class CapabilityItemId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "CAPABILITYID")
	private Integer capabilityId;
	
	@Column(name = "ITEMID")
    private Integer itemId;
	
	public CapabilityItemId(Integer capabilityId, Integer itemId) {
		this.capabilityId = capabilityId;
		this.itemId = itemId;
	}

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CapabilityItemId)) {
            return false;
        }
        CapabilityItemId capabilityItemId = (CapabilityItemId) o;
        return Objects.equals(capabilityId, capabilityItemId.capabilityId) 
        		&& Objects.equals(itemId, capabilityItemId.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capabilityId, itemId);
    }

}