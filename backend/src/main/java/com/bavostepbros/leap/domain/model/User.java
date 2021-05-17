package com.bavostepbros.leap.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.bavostepbros.leap.domain.service.roleservice.RoleService;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name = "USERID")
    private Integer userId;
    
    @Column (name = "ROLEID")
    private Integer roleId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    public User(String username, Integer roleId, String password) {       
        this.roleId = roleId;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" +
            " roleId='" + getUserId() + "'" +
            " roleId='" + getRoleId() + "'" +
            " roleId='" + getUsername() + "'" +
            ", description='" + getPassword() + "'" +
            "}";
    }
}
