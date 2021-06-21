package com.bavostepbros.leap.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
	
    @Id
    @GeneratedValue
    @Column(name = "ROLEID")
    private Integer roleId;
    
    @Column(name = "ROLENAME")
    private String roleName;
    
    @Column(name = "WEIGHT", unique = true)
    private Integer weight;
    
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();

    public Role(String roleName, Integer weight) {
        this.roleName = roleName;
        this.weight = weight;
    }

	public Role(Integer roleId, String roleName, Integer weight) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.weight = weight;
	}
    
}