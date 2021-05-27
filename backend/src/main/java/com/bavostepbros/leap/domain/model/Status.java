package com.bavostepbros.leap.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Status {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "STATUSID")
    private Integer statusId;
    
    @Column(name = "VALIDITYPERIOD")
    private LocalDate validityPeriod;
    
    @OneToOne(mappedBy = "status")
    private Project project;

    public Status(Integer statusId, LocalDate validityPeriod) {
		super();
		this.statusId = statusId;
		this.validityPeriod = validityPeriod;
	}
    
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