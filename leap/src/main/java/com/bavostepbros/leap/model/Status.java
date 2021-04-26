package com.bavostepbros.leap.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Status {

    @Id
    @GeneratedValue
    private Integer statusId;

    private Integer validityPeriod;

    public Status() {
    }

    public Status(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public Integer getStatusId() {
        return this.statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getValidityPeriod() {
        return this.validityPeriod;
    }

    public void setValidityPeriod(Integer validityPeriod) {
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