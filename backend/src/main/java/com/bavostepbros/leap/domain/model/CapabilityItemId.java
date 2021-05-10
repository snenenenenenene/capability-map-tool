package com.bavostepbros.leap.domain.model;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
*
* @author Bavo Van Meel
*
*/
@Getter
@Setter
public class CapabilityItemId implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer capability;
    private Integer strategyItem;

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CapabilityItemId)) {
            return false;
        }
        CapabilityItemId capabilityItemId = (CapabilityItemId) o;
        return Objects.equals(capability, capabilityItemId.capability) 
        && Objects.equals(strategyItem, capabilityItemId.strategyItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capability, strategyItem);
    }

}