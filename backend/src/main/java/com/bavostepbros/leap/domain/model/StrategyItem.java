package com.bavostepbros.leap.domain.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Type;

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
public class StrategyItem {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "ITEMID")
    private Integer itemId;

    @ManyToOne
    @JoinColumn(name = "STRATEGYID", nullable = false)
    private Strategy strategy;

    @NotBlank(message = "StrategyItem name is required.")
    @Column(name = "STRATEGYITEMNAME")
    private String strategyItemName;

    @NotBlank(message = "StrategyItem description is required.")
    @Type(type="text")
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "strategyItem")
    private List<CapabilityItem> capabilityItems;

    public StrategyItem(Strategy strategy, String strategyItemName, 
    		String description) {
        this.strategy = strategy;
        this.strategyItemName = strategyItemName;
        this.description = description;
    }
    
    public StrategyItem(Integer itemId, Strategy strategy, 
    		String strategyItemName, String description) {
		super();
		this.itemId = itemId;
		this.strategy = strategy;
		this.strategyItemName = strategyItemName;
		this.description = description;
	}

}