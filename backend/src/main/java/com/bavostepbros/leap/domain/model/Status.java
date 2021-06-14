package com.bavostepbros.leap.domain.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

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
public class Status {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "STATUSID")
    private Integer statusId;
    
    @NotNull(message = "Validity period must not be null.")
    @Column(name = "VALIDITYPERIOD", unique = true)
    private LocalDate validityPeriod;
    	
	@OneToOne(mappedBy = "status") 
	private Project project;

    public Status(Integer statusId, @NotNull LocalDate validityPeriod) {
		this.statusId = statusId;
		this.validityPeriod = validityPeriod;
	}
    
    public Status(@NotNull LocalDate validityPeriod) {
        this.validityPeriod = validityPeriod;
    }
}