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

/**
*
* @author Bavo Van Meel
*
*/
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// @IdClass(CapabilityItemId.class)
public class CapabilityItem {
		
	@EmbeddedId 
	private CapabilityItemId capabilityItemId;
	 
    @ManyToOne
    @MapsId("capabilityId")
    @EqualsAndHashCode.Include
    private Capability capability;

    @ManyToOne
    @MapsId("itemId")
    @EqualsAndHashCode.Include
    private StrategyItem strategyItem;
    
    @Column(name = "STRATEGICIMPORTANCE")
    private StrategicImportance strategicImportance;

    public CapabilityItem(Capability capability, StrategyItem strategyItem, 
    		StrategicImportance strategicImportance) {
    	this.capability = capability;
    	this.strategyItem = strategyItem;
        this.strategicImportance = strategicImportance;
        this.capabilityItemId = new CapabilityItemId(capability.getCapabilityId(), strategyItem.getItemId());
    }

    @Override
    public String toString() {
        return "{" +
            " capability='" + getCapability() + "'" +
            ", strategyItem='" + getStrategyItem() + "'" +
            ", strategicImportance='" + getStrategicImportance() + "'" +
            "}";
    }

}