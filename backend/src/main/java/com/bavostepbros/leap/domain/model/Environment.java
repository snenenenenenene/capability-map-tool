package com.bavostepbros.leap.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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
    
    @Column(name = "ENVIRONMENTNAME")
    private String environmentName;
    
    @OneToMany
    private List<Capability> capabilities;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="ENVIRONMENTID")
    private List<Strategy> strategies = new ArrayList<Strategy>();

    public Environment(String environmentName) {
        this.environmentName = environmentName;
    }
    
    public Environment(Integer environmentId, String environmentName) {
		this.environmentId = environmentId;
		this.environmentName = environmentName;
	}

	@Override
    public String toString() {
        return "{" +
            " environmentId='" + getEnvironmentId() + "'" +
            ", environmentName='" + getEnvironmentName() + "'" +
            "}";
    }

	

}