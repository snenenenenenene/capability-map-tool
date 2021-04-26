package com.bavostepbros.leap.model;

import java.io.Serializable;

public class CapabilityItemId implements Serializable {

    private Integer capabilityId;
    private Integer itemId;

    public Integer getCapabilityId() {
        return this.capabilityId;
    }

    public void setCapabilityId(Integer capabilityId) {
        this.capabilityId = capabilityId;
    }

    public Integer getItemId() {
        return this.itemId;
    }

    public void setItemId(Integer itemId) {
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