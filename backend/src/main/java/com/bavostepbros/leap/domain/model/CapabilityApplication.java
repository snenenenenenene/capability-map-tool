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
    private Double importance;
	
	@NotNull(message = "Efficiency support must not be null.")
	@Min(value = 1, message = "Efficiency support must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Efficiency support must be between 1 and 5, inclusive.")
	@Column(name = "EFFICIENCYSUPPORT")
    private Integer efficiencySupport;
	
	@NotNull(message = "Functional coverage must not be null.")
	@Min(value = 1, message = "Functional coverage must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Functional coverage must be between 1 and 5, inclusive.")
	@Column(name = "FUNCTIONALCOVERAGE")
    private Integer functionalCoverage;
	
	@NotNull(message = "Correctness business fit must not be null.")
	@Min(value = 1, message = "Correctness business fit must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Correctness business fit must be between 1 and 5, inclusive.")
	@Column(name = "CORRECTNESSBUSINESSFIT")
    private Integer correctnessBusinessFit;
	
	@NotNull(message = "Future potential must not be null.")
	@Min(value = 1, message = "Future potential must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Future potential must be between 1 and 5, inclusive.")
	@Column(name = "FUTUREPOTENTIAL")
    private Integer futurePotential;
	
	@NotNull(message = "Completeness must not be null.")
	@Min(value = 1, message = "Completeness must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Completeness must be between 1 and 5, inclusive.")
	@Column(name = "COMPLETENESS")
    private Integer completeness;
	
	@NotNull(message = "Correctness information fit support must not be null.")
	@Min(value = 1, message = "Correctness information fit must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Correctness information fit must be between 1 and 5, inclusive.")
	@Column(name = "CORRECTNESSINFORMATIONFIT")
    private Integer correctnessInformationFit;
	
	@NotNull(message = "Availability must not be null.")
	@Min(value = 1, message = "Availability must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Availability must be between 1 and 5, inclusive.")
	@Column(name = "AVAILABILITY")
    private Integer availability;

	public CapabilityApplication(Capability capability, ITApplication application, Double importance,
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
