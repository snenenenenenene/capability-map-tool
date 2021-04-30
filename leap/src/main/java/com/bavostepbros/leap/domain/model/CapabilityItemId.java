package com.bavostepbros.leap.domain.model;

import java.io.Serializable;
import java.util.Objects;

public class CapabilityItemId implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer capability;
    private Integer strategyItem;

    public Integer getCapability() {
        return this.capability;
    }

    public void setCapability(Integer capabilityId) {
        this.capability = capabilityId;
    }

    public Integer getstrategyItem() {
        return this.strategyItem;
    }

    public void setstrategyItem(Integer strategyItem) {
        this.strategyItem = strategyItem;
    }

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