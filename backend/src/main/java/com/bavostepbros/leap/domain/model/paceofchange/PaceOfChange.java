package com.bavostepbros.leap.domain.model.paceofchange;

public enum PaceOfChange {
	STANDARD(0), DIFFERENTIATION(1), INNOVATIVE(2);
	
	private int paceOfChangeValue;
	
	private PaceOfChange(int paceOfChangeValue) {
		this.paceOfChangeValue = paceOfChangeValue;
	}
	
	public Integer getPaceOfChangeValue() {
		return paceOfChangeValue;
	}
}
