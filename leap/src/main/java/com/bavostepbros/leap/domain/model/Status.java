package com.bavostepbros.leap.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Status {

    @Id
    @GeneratedValue
    @Column(name = "STATUSID")
    private Integer statusId;
    
    @Column(name = "VALIDITYPERIOD")
    private Integer validityPeriod;

    public Status(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    @Override
    public String toString() {
        return "{" +
            " statusId='" + getStatusId() + "'" +
            ", validityPeriod='" + getValidityPeriod() + "'" +
            "}";
    }

}