package com.bavostepbros.leap.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.bavostepbros.leap.domain.service.roleservice.RoleService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "EMAIL", unique = true)
    private String email;

    public User(String username, Integer roleId, String password, String email) {       
        this.roleId = roleId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "{" +
            " userId='" + getUserId() + "'" +
            " roleId='" + getRoleId() + "'" +
            " email= '" + getEmail() + "'" +
            " roleId='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
