package com.bavostepbros.leap.domain.model.strategicimportance;

public enum StrategicImportance {
	NONE(0), LOWEST(1), LOW(2), MEDIUM(3), HIGH(4), HIGHEST(5);
	
	private int importance;
	
	private StrategicImportance(int importance) {
		this.importance = importance;
	}
	
	public Integer getImportance() {
		return importance;
	}
	
	public boolean isEmpty() {
		return this.equals(StrategicImportance.NONE);
	}
	
	public boolean compare(Integer otherImportance) {
		return importance == otherImportance;
	}
	
	public static StrategicImportance getValue(Integer importance) {
		StrategicImportance[] importances = StrategicImportance.values();
		for (int i = 0; i < importances.length; i++) {
			if (importances[i].compare(importance)) {
				return importances[i];
			}
		}
		return StrategicImportance.NONE;
	}
}
