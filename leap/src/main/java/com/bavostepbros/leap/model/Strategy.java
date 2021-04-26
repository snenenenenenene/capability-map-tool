package com.bavostepbros.leap.model;

import java.sql.Date;

public class Strategy {

    @Id
    @GeneratedValue
    private Integer strategyId;

    @OneToOne(mappedBy="Status")
    private Integer statusId;

    private String strategyName;
    private Date timeFrameStart;
    private Date timeFrameEnd;

    @ManyToOne
    private Environment environment;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="STRATEGYID")
    private Set<StrategyItem> items;

    public Strategy() {
    }

    public Strategy(Integer statusId, String strategyName, Date timeFrameStart, Date timeFrameEnd) {
        this.statusId = statusId;
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

    public Integer getStatusId() {
        return this.statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
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
            ", statusId='" + getStatusId() + "'" +
            ", strategyName='" + getStrategyName() + "'" +
            ", timeFrameStart='" + getTimeFrameStart() + "'" +
            ", timeFrameEnd='" + getTimeFrameEnd() + "'" +
            "}";
    }

}