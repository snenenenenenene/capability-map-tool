package com.bavostepbros.leap.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ITApplication {

    @Id
    @GeneratedValue
    @Column(name = "ITAPPLICATIONID")
    private Integer id;

    @OneToOne
    @JoinColumn
    private Status status;

    @Column(name = "NAME")
    private String name;
    @Column(name = "TECHNOLOGY")
    private String technology;
    @Column(name = "VERSION")
    private String version;
    @Column(name = "PURCHASEDATE")
    private LocalDate purchaseDate;
    @Column(name = "ENDOFLIFE")
    private LocalDate endOfLife;
    @Column(name = "CURRENTSCALABLITY")
    private Byte currentScalability;
    @Column(name = "EXPECTEDSCALABILITY")
    private Byte expectedScalability;
    @Column(name = "CURRENTPERFORMANCE")
    private Byte currentPerformance;
    @Column(name = "EXPECTEDPERFORMANCE")
    private Byte expectedPerformance;
    @Column(name = "CURRENTSECURITYLEVEL")
    private Byte currentSecurityLevel;
    @Column(name = "EXPECTEDSECURITYLEVEL")
    private Byte expectedSecurityLevel;
    @Column(name = "CURRENTSTABILITY")
    private Byte currentStability;
    @Column(name = "EXPECTEDSTABILITY")
    private Byte expectedStability;
    @Column(name = "COSTCURRENCY")
    private String costCurrency;
    @Column(name = "CURRENTVALUE")
    private String currentValue;
    @Column(name = "CURRENTYEARLYCOST")
    private Double currentYearlyCost;
    @Column(name = "TIMEVALUE")
    private LocalDate timeValue;

    public ITApplication(Status status, String name, String technology, String version, LocalDate purchaseDate, LocalDate endOfLife, Byte currentScalability, Byte expectedScalability, Byte currentPerformance, Byte expectedPerformance, Byte currentSecurityLevel, Byte expectedSecurityLevel, Byte currentStability, Byte expectedStability, String costCurrency, String currentValue, Double currentYearlyCost, LocalDate timeValue) {
        this.status = status;
        this.name = name;
        this.technology = technology;
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
        this.costCurrency = costCurrency;
        this.currentValue = currentValue;
        this.currentYearlyCost = currentYearlyCost;
        this.timeValue = timeValue;
    }

    public ITApplication(String name, String technology, String version, LocalDate purchaseDate, LocalDate endOfLife, Byte currentScalability, Byte expectedScalability, Byte currentPerformance, Byte expectedPerformance, Byte currentSecurityLevel, Byte expectedSecurityLevel, Byte currentStability, Byte expectedStability, String costCurrency, String currentValue, Double currentYearlyCost, LocalDate timeValue) {
        this.name = name;
        this.technology = technology;
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
        this.costCurrency = costCurrency;
        this.currentValue = currentValue;
        this.currentYearlyCost = currentYearlyCost;
        this.timeValue = timeValue;
    }

    @Override
    public String toString() {
        return "ITApplication{" +
                "id=" + id +
                ", status=" + status +
                ", applicationName='" + name + '\'' +
                ", version='" + version + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", endOfLife=" + endOfLife +
                ", currentScalability='" + currentScalability + '\'' +
                ", expectedScalability='" + expectedScalability + '\'' +
                ", currentPerformance='" + currentPerformance + '\'' +
                ", expectedPerformance='" + expectedPerformance + '\'' +
                ", currentSecurityLevel='" + currentSecurityLevel + '\'' +
                ", expectedSecurityLevel='" + expectedSecurityLevel + '\'' +
                ", currentStability='" + currentStability + '\'' +
                ", expectedStability='" + expectedStability + '\'' +
                ", costCurrency='" + costCurrency + '\'' +
                ", currentValue='" + currentValue + '\'' +
                ", currentYearlyCost=" + currentYearlyCost +
                ", timeValue=" + timeValue +
                '}';
    }
}
