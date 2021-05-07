package com.bavostepbros.leap.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StrategyItem {

    @Id
    @GeneratedValue
    @Column(name = "ITEMID")
    private Integer itemId;

    @ManyToOne
    private Strategy strategy;

    @Column(name = "STRATEGYITEMNAME")
    private String strategyItemName;

    @Type(type="text")
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany
    private List<CapabilityItem> capabilityItems;

    public StrategyItem(Strategy strategy, String strategyItemName, String description) {
        this.strategy = strategy;
        this.strategyItemName = strategyItemName;
        this.description = description;
    }

    @Override
    public String toString() {
        return "{" +
            " itemId='" + getItemId() + "'" +
            ", strategy='" + getStrategy() + "'" +
            ", strategyItemName='" + getStrategyItemName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }

}