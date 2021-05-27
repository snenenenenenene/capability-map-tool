package com.bavostepbros.leap.domain.model.timevalue;

public enum TimeValue {
	TOLERATE(0), INVEST(1), ELIMINATE(2), MIGRATE(3);
	
	private int value;
	
	private TimeValue(int value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
}
