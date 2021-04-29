package com.bavostepbros.leap.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Strategy {

    @Id
    @GeneratedValue
    private Integer strategyId;

    @OneToOne
    @JoinColumn
    private Status status;

    private String strategyName;
    private Date timeFrameStart;
    private Date timeFrameEnd;

    @ManyToOne
    private Environment environment;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="STRATEGYID")
    private List<StrategyItem> items = new ArrayList<StrategyItem>();

    public Strategy() {
    }

    public Strategy(Status status, String strategyName, Date timeFrameStart, Date timeFrameEnd) {
        this.status = status;
        this.strategyName = strategyName;
        this.timeFrameStart = timeFrameStart;
        this.timeFrameEnd = timeFrameEnd;
    }

    public Integer getStrategyId() {
        return this.strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStrategyName() {
        return this.strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Date getTimeFrameStart() {
        return this.timeFrameStart;
    }

    public void setTimeFrameStart(Date timeFrameStart) {
        this.timeFrameStart = timeFrameStart;
    }

    public Date getTimeFrameEnd() {
        return this.timeFrameEnd;
    }

    public void setTimeFrameEnd(Date timeFrameEnd) {
        this.timeFrameEnd = timeFrameEnd;
    }

    @Override
    public String toString() {
        return "{" +
            " strategyId='" + getStrategyId() + "'" +
            ", statusId='" + getStatus() + "'" +
            ", strategyName='" + getStrategyName() + "'" +
            ", timeFrameStart='" + getTimeFrameStart() + "'" +
            ", timeFrameEnd='" + getTimeFrameEnd() + "'" +
            "}";
    }

}