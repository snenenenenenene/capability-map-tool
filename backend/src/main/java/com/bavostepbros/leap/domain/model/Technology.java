package com.bavostepbros.leap.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Technology {
	
	@Id
	@GeneratedValue
	@Column(name = "TECHNOLOGYID")
	private Integer technologyId;
	
	@Column(name = "TECHNOLOGYNAME", unique = true, nullable = false)
	private String technologyName;
	
	public Technology(String technologyName) {
		this.technologyName = technologyName;
	}
}
