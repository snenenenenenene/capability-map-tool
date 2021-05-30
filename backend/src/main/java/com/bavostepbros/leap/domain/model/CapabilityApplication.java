package com.bavostepbros.leap.domain.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CapabilityApplication {
	
	@EmbeddedId
	private CapabilityApplicationId capabilityApplicationId;
	
	@ManyToOne
    @MapsId("capabilityId")
    @EqualsAndHashCode.Include
    private Capability capability;
	
	@ManyToOne
    @MapsId("applicationId")
    @EqualsAndHashCode.Include
    private ITApplication application;
	
	@Column(name = "IMPORTANCE")
    private Integer importance;
	
	@NotNull
	@Min(1)
	@Max(5)
	@Column(name = "EFFICIENCYSUPPORT")
    private Integer efficiencySupport;
	
	@Column(name = "FUNCTIONALCOVERAGE")
    private Integer functionalCoverage;
	
	@Column(name = "CORRECTNESSBUSINESSFIT")
    private Integer correctnessBusinessFit;
	
	@Column(name = "FUTUREPOTENTIAL")
    private Integer futurePotential;
	
	@Column(name = "COMPLETENESS")
    private Integer completeness;
	
	@Column(name = "CORRECTNESSINFORMATIONFIT")
    private Integer correctnessInformationFit;
	
	@Column(name = "AVAILABILITY")
    private Integer availability;

	public CapabilityApplication(Capability capability, ITApplication application, Integer importance,
			Integer efficiencySupport, Integer functionalCoverage, Integer correctnessBusinessFit,
			Integer futurePotential, Integer completeness, Integer correctnessInformationFit, Integer availability) {
		this.capability = capability;
		this.application = application;
		this.importance = importance;
		this.efficiencySupport = efficiencySupport;
		this.functionalCoverage = functionalCoverage;
		this.correctnessBusinessFit = correctnessBusinessFit;
		this.futurePotential = futurePotential;
		this.completeness = completeness;
		this.correctnessInformationFit = correctnessInformationFit;
		this.availability = availability;
		this.capabilityApplicationId = new CapabilityApplicationId(capability.getCapabilityId(), 
				application.getItApplicationId());
	}
	
	
}
