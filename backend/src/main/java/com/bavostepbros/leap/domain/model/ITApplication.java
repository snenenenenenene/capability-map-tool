package com.bavostepbros.leap.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bavostepbros.leap.domain.model.timevalue.TimeValue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ITApplication {

    @Id
    @GeneratedValue

/** 
 * @return Integer
 */

/** 
 * @return Status
 */

/** 
 * @return String
 */

/** 
 * @return String
 */

/** 
 * @return LocalDate
 */

/** 
 * @return LocalDate
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return String
 */

/** 
 * @return Double
 */

/** 
 * @return Integer
 */

/** 
 * @return Double
 */

/** 
 * @return Double
 */

/** 
 * @return TimeValue
 */

/** 
 * @return List<CapabilityApplication>
 */

/** 
 * @return List<Technology>
 */
    @EqualsAndHashCode.Include
    @Column(name = "ITAPPLICATIONID")
    private Integer itApplicationId;

/** 
 * @return boolean
 */

/** 
 * @return boolean
 */

/** 
 * @return int
 */

    @OneToOne
    @JoinColumn
    private Status status;
    
    @NotBlank(message = "ItApplication name is required.")
    @Column(name = "NAME", unique = true)
    private String name;
    
    @NotBlank(message = "Version is required.")
    @Column(name = "VERSION")
    private String version;
    
    @NotNull(message = "Purchase date must not be null.")
    @Column(name = "PURCHASEDATE")
    private LocalDate purchaseDate;
    
    @NotNull(message = "End of life must not be null.")
    @Column(name = "ENDOFLIFE")
    private LocalDate endOfLife;
    
    @NotNull(message = "Current scalability must not be null.")
	@Min(value = 1, message = "Current scalability must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Current scalability must be between 1 and 5, inclusive.")
    @Column(name = "CURRENTSCALABLITY")
    private Integer currentScalability;
    
    @NotNull(message = "Expected scalability must not be null.")
	@Min(value = 1, message = "Expected scalability must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Expected scalability must be between 1 and 5, inclusive.")
    @Column(name = "EXPECTEDSCALABILITY")
    private Integer expectedScalability;
    
    @NotNull(message = "Current performance must not be null.")
	@Min(value = 1, message = "Current performance must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Current performance must be between 1 and 5, inclusive.")
    @Column(name = "CURRENTPERFORMANCE")
    private Integer currentPerformance;
    
    @NotNull(message = "Expected performance must not be null.")
	@Min(value = 1, message = "Expected performance must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Expected performance must be between 1 and 5, inclusive.")
    @Column(name = "EXPECTEDPERFORMANCE")
    private Integer expectedPerformance;
    
    @NotNull(message = "Current security level must not be null.")
	@Min(value = 1, message = "Current security level must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Current security level must be between 1 and 5, inclusive.")
    @Column(name = "CURRENTSECURITYLEVEL")
    private Integer currentSecurityLevel;
    
    @NotNull(message = "Expected security level must not be null.")
	@Min(value = 1, message = "Expected security level must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Expected security level must be between 1 and 5, inclusive.")
    @Column(name = "EXPECTEDSECURITYLEVEL")
    private Integer expectedSecurityLevel;
    
    @NotNull(message = "Current stability must not be null.")
	@Min(value = 1, message = "Current stability must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Current stability must be between 1 and 5, inclusive.")
    @Column(name = "CURRENTSTABILITY")
    private Integer currentStability;
    
    @NotNull(message = "Expected stability must not be null.")
	@Min(value = 1, message = "Expected stability must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Expected stability must be between 1 and 5, inclusive.")
    @Column(name = "EXPECTEDSTABILITY")
    private Integer expectedStability;
    
    @Column(name = "CURRENCYTYPE")
    private String currencyType;
    
    @Column(name = "COSTCURRENCY")
    private Double costCurrency;
    
    @NotNull(message = "Current value must not be null.")
	@Min(value = 1, message = "Current value must be between 1 and 5, inclusive.")
    @Max(value = 5, message = "Current value must be between 1 and 5, inclusive.")
    @Column(name = "CURRENTVALUE")
    private Integer currentValue;
    
    @NotNull(message = "Current yearly cost must not be null.")
    @Column(name = "CURRENTYEARLYCOST")
    private Double currentYearlyCost;
    
    @NotNull(message = "Accepted yearly cost must not be null.")
    @Column(name = "ACCEPTEDYEARLYCOST")
    private Double acceptedYearlyCost;
    
    @NotNull(message = "Time value must not be null.")
    @Column(name = "TIMEVALUE")
    private TimeValue timeValue;
    
    @OneToMany(mappedBy = "application")
    private List<CapabilityApplication> capabilityApplication;
    
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "ITAPPLICATION_TECHNOLOGY", 
    	joinColumns = {@JoinColumn(name = "ITAPPLICATIONID")}, 
    	inverseJoinColumns = {@JoinColumn(name = "TECHNOLOGYID")})
    private List<Technology> technologies = new ArrayList<Technology>();
    
    public ITApplication(Integer itApplicationId, Status status, String name, String version, LocalDate purchaseDate,
			LocalDate endOfLife, Integer currentScalability, Integer expectedScalability, Integer currentPerformance,
			Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel,
			Integer currentStability, Integer expectedStability, String currencyType, Double costCurrency, Integer currentValue,
			Double currentYearlyCost, Double acceptedYearlyCost, TimeValue timeValue) {
    	this.itApplicationId = itApplicationId;
		this.status = status;
		this.name = name;
		this.version = version;
		this.purchaseDate = purchaseDate;
		this.endOfLife = endOfLife;
		this.currentScalability = currentScalability;
		this.expectedScalability = expectedScalability;
		this.currentPerformance = currentPerformance;
		this.expectedPerformance = expectedPerformance;
		this.currentSecurityLevel = currentSecurityLevel;
		this.expectedSecurityLevel = expectedSecurityLevel;
		this.currentStability = currentStability;
		this.expectedStability = expectedStability;
		this.currencyType = currencyType;
		this.costCurrency = costCurrency;
		this.currentValue = currentValue;
		this.currentYearlyCost = currentYearlyCost;
		this.acceptedYearlyCost = acceptedYearlyCost;
		this.timeValue = timeValue;
	}

	public ITApplication(Status status, String name, String version, LocalDate purchaseDate,
			LocalDate endOfLife, Integer currentScalability, Integer expectedScalability, Integer currentPerformance,
			Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel,
			Integer currentStability, Integer expectedStability, String currencyType, Double costCurrency, Integer currentValue,
			Double currentYearlyCost, Double acceptedYearlyCost, TimeValue timeValue) {
		this.status = status;
		this.name = name;
		this.version = version;
		this.purchaseDate = purchaseDate;
		this.endOfLife = endOfLife;
		this.currentScalability = currentScalability;
		this.expectedScalability = expectedScalability;
		this.currentPerformance = currentPerformance;
		this.expectedPerformance = expectedPerformance;
		this.currentSecurityLevel = currentSecurityLevel;
		this.expectedSecurityLevel = expectedSecurityLevel;
		this.currentStability = currentStability;
		this.expectedStability = expectedStability;
		this.currencyType = currencyType;
		this.costCurrency = costCurrency;
		this.currentValue = currentValue;
		this.currentYearlyCost = currentYearlyCost;
		this.acceptedYearlyCost = acceptedYearlyCost;
		this.timeValue = timeValue;
	}
	
	
    /** 
     * @param technology
     */
    public void addTechnology(Technology technology) {
    	technologies.add(technology);
    	return;
    }
	
	
    /** 
     * @param technology
     */
    public void removeTechnology(Technology technology) {
		technologies.remove(technology);
	}
    
}
