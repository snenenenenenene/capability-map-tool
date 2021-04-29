package com.bavostepbros.leap.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Environment {

    @Id
    @GeneratedValue
    @Column(name = "ENVIRONMENTID")
    private Integer environmentId;
    
    @Column(name = "ENVIRONMENTNAME")
    private String environmentName;
    
    @OneToMany
    private List<Capability> capabilities = new ArrayList<Capability>();

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="ENVIRONMENTID")
    private List<Strategy> strategies = new ArrayList<Strategy>();

    public Environment() {
    }

    public Environment(String environmentName) {
        this.environmentName = environmentName;
    }

	public Integer getEnvironmentId() {
        return this.environmentId;
    }

    public void setEnvironmentId(Integer environmentId) {
        this.environmentId = environmentId;
    }

    public String getEnvironmentName() {
        return this.environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public List<Capability> getCapabilities() {
		return capabilities;
	}

	@Override
    public String toString() {
        return "{" +
            " environmentId='" + getEnvironmentId() + "'" +
            ", environmentName='" + getEnvironmentName() + "'" +
            "}";
    }

}