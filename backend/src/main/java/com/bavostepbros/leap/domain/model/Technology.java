package com.bavostepbros.leap.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

/** 
 * @return Integer
 */

/** 
 * @return String
 */

/** 
 * @return List<ITApplication>
 */
@Getter
@Setter
@NoArgsConstructor
public class Technology {
	
	@Id
	@GeneratedValue
	@Column(name = "TECHNOLOGYID")
	private Integer technologyId;
	
	@NotBlank(message = "Technology name is required.")
	@Column(name = "TECHNOLOGYNAME", unique = true)
	private String technologyName;
	
	@ManyToMany(mappedBy = "technologies")
	private List<ITApplication> itApplications = new ArrayList<ITApplication>();
		
	public Technology(String technologyName) {
		this.technologyName = technologyName;
	}
	
	public Technology(Integer technologyId, String technologyName) {
		this.technologyId = technologyId;
		this.technologyName = technologyName;
	}
}
