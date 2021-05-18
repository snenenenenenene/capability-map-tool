package com.bavostepbros.leap.domain.model.capabilitylevel;

/**
*
* @author Bavo Van Meel
*
*/
public enum CapabilityLevel {
	NONE(0), ONE(1), TWO(2), THREE(3);
	
	private int level;

	private CapabilityLevel(int level) {
		this.level = level;
	}
	
	public Integer getLevel() {
		return level;
	}
	
	public boolean isEmpty() {
		return this.equals(CapabilityLevel.NONE);
	}
	
	public boolean compare(Integer otherLevel) {
		return level == otherLevel;
	}
	
	public static CapabilityLevel getValue(Integer level) {
		CapabilityLevel[] levels = CapabilityLevel.values();
		for (int i = 0; i < levels.length; i++) {
			if (levels[i].compare(level)) {
				return levels[i];
			}
		}
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
}
