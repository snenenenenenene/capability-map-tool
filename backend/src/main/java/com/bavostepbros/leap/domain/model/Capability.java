package com.bavostepbros.leap.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;

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
public class Capability {

	@Id
	@GeneratedValue
	@EqualsAndHashCode.Include
	@Column(name = "CAPABILITYID")
	private Integer capabilityId;

	@ManyToOne
	@NotNull(message = "Validity period must not be null.")
	@JoinColumn(name = "ENVIRONMENTID", nullable = false)
	private Environment environment;

	@OneToOne
	@NotNull(message = "Validity period must not be null.")
	@JoinColumn
	private Status status;

	@PositiveOrZero(message = "The capability id must be positive.")
	@Column(name = "PARENTCAPABILITYID")
	private Integer parentCapabilityId;

	@NotBlank(message = "Capability name is required.")
	@Column(name = "CAPABILITYNAME", unique = true)
	private String capabilityName;

	@Column(name = "CAPABILITYDESCRIPTION")
	private String capabilityDescription;

	@Column(name = "LEVEL")
	private CapabilityLevel level;

	@NotNull(message = "Pace of change must not be null.")
	@Column(name = "PACEOFCHANGE")
	private PaceOfChange paceOfChange;

	@NotNull(message = "Target operating model must not be null.")
	@Column(name = "TARGETOPERATINGMODEL")
	private TargetOperatingModel targetOperatingModel;

	@NotNull(message = "Resource quality must not be null.")
	@Min(value = 1, message = "Resource quality must be between 1 and 5, inclusive.")
	@Max(value = 5, message = "Resource quality must be between 1 and 5, inclusive.")
	@Column(name = "RESOURCEQUALITY")
	private Integer resourceQuality;

	@Column(name = "INFORMATIONQUALITY")
	private Integer informationQuality;

	@Column(name = "APPLICATIONFIT")
	private Integer applicationFit;

	@OneToMany(mappedBy = "capability")
	private List<CapabilityItem> capabilityItems;

	@OneToMany(mappedBy = "capability")
	private List<CapabilityApplication> capabilityApplication;

	@OneToMany(mappedBy = "capability")
	private List<CapabilityInformation> capabilityInformation;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "CAPABILITY_PROJECT",
		joinColumns = { @JoinColumn(name = "CAPABILITYID") },
		inverseJoinColumns = {@JoinColumn(name = "PROJECTID") },
		uniqueConstraints = { @UniqueConstraint(columnNames = {"CAPABILITYID", "PROJECTID"})})
	private List<Project> projects;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "CAPABILITY_BUSINESSPROCESS",
		joinColumns = { @JoinColumn(name = "CAPABILITYID") },
		inverseJoinColumns = { @JoinColumn(name = "BUSINESSPROCESSID") },
		uniqueConstraints = { @UniqueConstraint(columnNames = {"CAPABILITYID", "BUSINESSPROCESSID"})})
	private Set<BusinessProcess> businessProcess = new HashSet<>();

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "CAPABILITY_RESOURCE",
		joinColumns = { @JoinColumn(name = "CAPABILITYID") },
		inverseJoinColumns = { @JoinColumn(name = "RESOURCEID") },
		uniqueConstraints = { @UniqueConstraint(columnNames = {"CAPABILITYID", "RESOURCEID"})})
	private List<Resource> resources;

	public Capability(Environment environment, Status status, Integer parentCapabilityId, @NotBlank String capabilityName,
			String capabilityDescription, PaceOfChange paceOfChange, TargetOperatingModel targetOperatingModel,
			Integer resourceQuality, Integer informationQuality, Integer applicationFit) {
		this.environment = environment;
		this.status = status;
		this.parentCapabilityId = parentCapabilityId;
		this.capabilityName = capabilityName;
		this.capabilityDescription = capabilityDescription;
		this.paceOfChange = paceOfChange;
		this.targetOperatingModel = targetOperatingModel;
		this.resourceQuality = resourceQuality;
		this.informationQuality = informationQuality;
		this.applicationFit = applicationFit;
	}

	public Capability(Integer capabilityId, Environment environment, Status status, Integer parentCapabilityId,
			@NotBlank String capabilityName, String capabilityDescription, PaceOfChange paceOfChange,
			TargetOperatingModel targetOperatingModel, Integer resourceQuality, Integer informationQuality,
			Integer applicationFit) {
		this.capabilityId = capabilityId;
		this.environment = environment;
		this.status = status;
		this.parentCapabilityId = parentCapabilityId;
		this.capabilityName = capabilityName;
		this.capabilityDescription = capabilityDescription;
		this.paceOfChange = paceOfChange;
		this.targetOperatingModel = targetOperatingModel;
		this.resourceQuality = resourceQuality;
		this.informationQuality = informationQuality;
		this.applicationFit = applicationFit;
	}

	public Capability(Integer capabilityId, Environment environment, Status status, Integer parentCapabilityId) {
		this.capabilityId = capabilityId;
		this.environment = environment;
		this.status = status;
		this.parentCapabilityId = parentCapabilityId;
		this.capabilityName = "Default " + capabilityId;
		this.paceOfChange = PaceOfChange.STANDARD;
		this.targetOperatingModel = TargetOperatingModel.COORDINATION;
		this.resourceQuality = 1;
	}

	public void addProject(Project project) {
		projects.add(project);
		project.getCapabilities().add(this);
	}

	public void removeProject(Project project) {
		projects.remove(project);
		project.getCapabilities().remove(this);
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void addBusinessProcess(BusinessProcess businessProcessItem) {
		businessProcess.add(businessProcessItem);
		businessProcessItem.getCapabilities().add(this);
	}

	public void removeBusinessProcess(BusinessProcess businessProcessItem) {
		businessProcess.remove(businessProcessItem);
		businessProcessItem.getCapabilities().remove(this);
	}

	public Set<BusinessProcess> getBusinessProcess() {
		return businessProcess;
	}

	public void addResource(Resource resource) {
		resources.add(resource);
		resource.getCapabilities().add(this);
	}

	public void removeResource(Resource resource) {
		resources.remove(resource);
		resource.getCapabilities().remove(this);
	}

	public List<Resource> getResources() {
		return resources;
	}

}