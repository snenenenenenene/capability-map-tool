package com.bavostepbros.leap.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Status {

    @Id
    @GeneratedValue
    @Column(name = "STATUSID")
    private Integer statusId;
    
    @Column(name = "VALIDITYPERIOD")
    private LocalDate validityPeriod;

    public Status(LocalDate validityPeriod) {
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