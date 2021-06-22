package com.bavostepbros.leap.domain.model.capabilitylevel;

public enum CapabilityLevel {
	NONE(0), ONE(1), TWO(2), THREE(3);
	
	private int level;

	private CapabilityLevel(int level) {
		this.level = level;
	}
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public boolean isEmpty() {
		return this.equals(CapabilityLevel.NONE);
	}
	
	public boolean compare(Integer otherLevel) {
		return level == otherLevel;
	}
	
	public static CapabilityLevel getValue(Integer level) {
		for (CapabilityLevel i : CapabilityLevel.values())
			if (i.compare(level)) return i;
		return CapabilityLevel.NONE;
	}
	
	public static Integer getMax() {
		Integer max = Integer.MIN_VALUE;
		CapabilityLevel[] levels = CapabilityLevel.values();
		for (int i = 0; i < levels.length; i++) {
			if (i > max) {
				max = i;
			}
		}
		return max;
	}

	public CapabilityLevel next() {
		return values()[this.ordinal() + 1];
	}
}
