package com.bavostepbros.leap.domain.model.targetoperatingmodel;

public enum TargetOperatingModel {
	COORDINATION(0), UNIFICATION(1), DIVERSIFICATION(2), REPLICATION(3);
	
	private int targetOperatingModelValue;
	
	private TargetOperatingModel(int targetOperatingModelValue) {
		this.targetOperatingModelValue = targetOperatingModelValue;
	}
	
	public Integer getTargetOperatingModelValue() {
		return targetOperatingModelValue;
	}
}
