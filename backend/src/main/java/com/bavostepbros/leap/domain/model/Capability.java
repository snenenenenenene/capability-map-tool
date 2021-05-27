package com.bavostepbros.leap.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
public class Capability {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "CAPABILITYID")
    private Integer capabilityId;
    
    @ManyToOne
    private Environment environment;

    @OneToOne
    @JoinColumn
    private Status status;
    
    @Column(name = "PARENTCAPABILITYID")
    private Integer parentCapabilityId;
    
    @Column(name = "CAPABILITYNAME")
    private String capabilityName;
    
    @Column(name = "LEVEL")
    private CapabilityLevel level;
    
    @Column(name = "PACEOFCHANGE")
    private boolean paceOfChange;
    
    @Column(name = "TARGETOPERATINGMODEL")
    private String targetOperatingModel;
    
    @Column(name = "RESOURCEQUALITY")
    private Integer resourceQuality;
    
    @Column(name = "INFORMATIONQUALITY")
    private Integer informationQuality;
    
    @Column(name = "APPLICATIONFIT")
    private Integer applicationFit;

    @OneToMany
    private List<CapabilityItem> capabilityItems;
    
    @OneToMany
    private List<CapabilityApplication> capabilityApplication;

    public Capability(Environment environment, Status status, Integer parentCapabilityId, String capabilityName, 
    		boolean paceOfChange, String targetOperatingModel, Integer resourceQuality,
    		Integer informationQuality, Integer applicationFit) {
        this.environment = environment;
        this.status = status;
        this.parentCapabilityId = parentCapabilityId;
        this.capabilityName = capabilityName;
        this.paceOfChange = paceOfChange;
        this.targetOperatingModel = targetOperatingModel;
        this.resourceQuality = resourceQuality;
        this.informationQuality = informationQuality;
        this.applicationFit = applicationFit;
    }
    
    public Capability(Integer capabilityId, Environment environment, Status status, Integer parentCapabilityId,
			String capabilityName, boolean paceOfChange, String targetOperatingModel,
			Integer resourceQuality, Integer informationQuality, Integer applicationFit) {
		super();
		this.capabilityId = capabilityId;
		this.environment = environment;
		this.status = status;
		this.parentCapabilityId = parentCapabilityId;
		this.capabilityName = capabilityName;
		this.paceOfChange = paceOfChange;
		this.targetOperatingModel = targetOperatingModel;
		this.resourceQuality = resourceQuality;
		this.informationQuality = informationQuality;
		this.applicationFit = applicationFit;
	}

    @Override
    public String toString() {
        return "{" +
            " capabilityId='" + getCapabilityId() + "'" +
            ", environment='" + getEnvironment() + "'" +
            ", status='" + getStatus() + "'" +
            ", parentCapabilityId='" + getParentCapabilityId() + "'" +
            ", name='" + getCapabilityName() + "'" +
            ", level='" + getLevel() + "'" +
            ", paceOfChange='" + isPaceOfChange() + "'" +
            ", targetOperatingModel='" + getTargetOperatingModel() + "'" +
            ", resourceQuality='" + getResourceQuality() + "'" +
            ", informationQuality='" + getInformationQuality() + "'" +
            ", applicationFit='" + getApplicationFit() + "'" +
            "}";
    }
    
}