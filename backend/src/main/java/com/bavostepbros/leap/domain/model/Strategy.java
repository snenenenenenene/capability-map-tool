package com.bavostepbros.leap.domain.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
 * @return LocalDate
 */

/** 
 * @return LocalDate
 */

/** 
 * @return Environment
 */

/** 
 * @return List<StrategyItem>
 */
@Getter
@Setter
@NoArgsConstructor

/** 
 * @return boolean
 */

/** 
 * @return boolean
 */

/** 
 * @return int
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Strategy {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "STRATEGYID")
    private Integer strategyId;

    @OneToOne
    @JoinColumn
    private Status status;
    
    @NotBlank(message = "Strategy name is required.")
    @Column(name = "STRATEGYNAME")
    private String strategyName;

    @NotNull(message = "Time frame start must not be null.")
    @Column(name = "TIMEFRAMESTART")
    private LocalDate timeFrameStart;
    
    @NotNull(message = "Time frame end must not be null.")
    @Column(name = "TIMEFRAMEEND")
    private LocalDate timeFrameEnd;

    @ManyToOne
    @JoinColumn(name = "ENVIRONMENTID", nullable = false)
    private Environment environment;

    @OneToMany(mappedBy = "strategy")
    private List<StrategyItem> items;

    public Strategy(Status status, String strategyName, LocalDate timeFrameStart, 
    		LocalDate timeFrameEnd, Environment environment) {
        this.status = status;
        this.strategyName = strategyName;
        this.timeFrameStart = timeFrameStart;
        this.timeFrameEnd = timeFrameEnd;
        this.environment = environment;
    }
    
    public Strategy(Integer strategyId, Status status, String strategyName, LocalDate timeFrameStart,
			LocalDate timeFrameEnd, Environment environment) {
		this.strategyId = strategyId;
		this.status = status;
		this.strategyName = strategyName;
		this.timeFrameStart = timeFrameStart;
		this.timeFrameEnd = timeFrameEnd;
		this.environment = environment;
	}

}