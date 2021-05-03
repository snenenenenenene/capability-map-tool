package com.bavostepbros.leap.domain.model.capabilitylevel;

public enum CapabilityLevel {
	ONE(1), TWO(2), THREE(3);
	
	private int level;

	private CapabilityLevel(int level) {
		this.level = level;
	}
	
	public Integer getLevel() {
		return level;
	}
}
