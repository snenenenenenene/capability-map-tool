package com.bavostepbros.leap.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import java.sql.Blob;
import java.util.List;

@Entity
public class Capability {

    @Id
    @GeneratedValue
    private Integer capabilityId;

    @ManyToOne
    private Integer environmentId;

    @OneToOne
    private Integer statusId;

    private Integer parentCapabilityId;
    private String capabilityName;
    private Integer level;
    private boolean paceOfChange;
    private Blob targetOperatingModel;
    private Integer resourceQuality;
    private Integer informationQuality;
    private Integer applicationFit;

    @OneToMany(mappedBy = "Capability")
    private List<CapabilityItem> capabilityItems;

    public Capability() {
    }

    public Capability(Integer environmentId, Integer statusId, Integer parentCapabilityId, String capabilityName, 
    Integer level, boolean paceOfChange, Blob targetOperatingModel, Integer resourceQuality, 
    Integer informationQuality, Integer applicationFit) {
        this.environmentId = environmentId;
        this.statusId = statusId;
        this.parentCapabilityId = parentCapabilityId;
        this.capabilityName = capabilityName;
        this.level = level;
        this.paceOfChange = paceOfChange;
        this.targetOperatingModel = targetOperatingModel;
        this.resourceQuality = resourceQuality;
        this.informationQuality = informationQuality;
        this.applicationFit = applicationFit;
    }

    public Integer getCapabilityId() {
        return this.capabilityId;
    }

    public void setCapabilityId(Integer capabilityId) {
        this.capabilityId = capabilityId;
    }

    public Integer getEnvironmentId() {
        return this.environmentId;
    }

    public void setEnvironmentId(Integer environmentId) {
        this.environmentId = environmentId;
    }

    public Integer getStatusId() {
        return this.statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getParentCapabilityId() {
        return this.parentCapabilityId;
    }

    public void setParentCapabilityId(Integer parentCapabilityId) {
        this.parentCapabilityId = parentCapabilityId;
    }

    public String getCapabilityName() {
        return this.capabilityName;
    }

    public void setCapabilityName(String capabilityName) {
        this.capabilityName = capabilityName;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public boolean isPaceOfChange() {
        return this.paceOfChange;
    }

    public boolean getPaceOfChange() {
        return this.paceOfChange;
    }

    public void setPaceOfChange(boolean paceOfChange) {
        this.paceOfChange = paceOfChange;
    }

    public Blob getTargetOperatingModel() {
        return this.targetOperatingModel;
    }

    public void setTargetOperatingModel(Blob targetOperatingModel) {
        this.targetOperatingModel = targetOperatingModel;
    }

    public Integer getResourceQuality() {
        return this.resourceQuality;
    }

    public void setResourceQuality(Integer resourceQuality) {
        this.resourceQuality = resourceQuality;
    }

    public Integer getInformationQuality() {
        return this.informationQuality;
    }

    public void setInformationQuality(Integer informationQuality) {
        this.informationQuality = informationQuality;
    }

    public Integer getApplicationFit() {
        return this.applicationFit;
    }

    public void setApplicationFit(Integer applicationFit) {
        this.applicationFit = applicationFit;
    }

    @Override
    public String toString() {
        return "{" +
            " capabilityId='" + getCapabilityId() + "'" +
            ", environmentId='" + getEnvironmentId() + "'" +
            ", statusId='" + getStatusId() + "'" +
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