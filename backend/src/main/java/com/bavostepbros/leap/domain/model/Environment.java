package com.bavostepbros.leap.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

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
public class Environment {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "ENVIRONMENTID")
    private Integer environmentId;

    @NotBlank(message = "Environment name is required.")
    @Column(name = "ENVIRONMENTNAME", unique = true)
    private String environmentName;
    
    @OneToMany(mappedBy = "environment")
    private List<Capability> capabilities;

    @OneToMany(mappedBy = "environment")
    private List<Strategy> strategies;

    public Environment(String environmentName) {
        this.environmentName = environmentName;
        this.capabilities = new ArrayList<>();
        this.strategies = new ArrayList<>();
    }

    public Environment(Integer environmentId, String environmentName) {
		this.environmentId = environmentId;
		this.environmentName = environmentName;
		this.capabilities = new ArrayList<>();
		this.strategies = new ArrayList<>();
	}
}
