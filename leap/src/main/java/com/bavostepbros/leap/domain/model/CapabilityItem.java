package com.bavostepbros.leap.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(CapabilityItemId.class)
public class CapabilityItem {

    @Id 
    @ManyToOne
    @JoinColumn(name = "Capability_capabilityId", referencedColumnName = "capabilityId")
    private Capability capability;

    @Id 
    @ManyToOne
    @JoinColumn(name = "StrategyItem_itemId", referencedColumnName = "itemId")
    private StrategyItem strategyItem;

    private Integer strategicImportance;

    public CapabilityItem(Integer strategicImportance) {
        this.strategicImportance = strategicImportance;
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