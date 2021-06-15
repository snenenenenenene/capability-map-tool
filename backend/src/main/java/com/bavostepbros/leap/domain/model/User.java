package com.bavostepbros.leap.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "USERID")
    private Integer userId;
    
    @Column (name = "ROLEID")
    private Integer roleId;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL", unique = true)
    private String email;

    public User(String username, Integer roleId, String password, String email) {       
        this.roleId = roleId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

}
