package com.bavostepbros.leap.domain.model.capabilitylevel;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
*
* @author Bavo Van Meel
*
*/
@Converter(autoApply = true)
public class CapabilityLevelConverter implements AttributeConverter<CapabilityLevel, Integer> {

	
	/** 
	 * @param capabilityLevel
	 * @return Integer
	 */
	@Override
	public Integer convertToDatabaseColumn(CapabilityLevel capabilityLevel) {
		if (capabilityLevel == null) {
			return null;
		}		
		return capabilityLevel.getLevel();
	}

	
	/** 
	 * @param level
	 * @return CapabilityLevel
	 */
	@Override
	public CapabilityLevel convertToEntityAttribute(Integer level) {
		if (level == null) {
			return null;
		}
		
		CapabilityLevel capabilityLevel = Stream.of(CapabilityLevel.values())
				.filter(l -> l.getLevel().equals(level))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		return capabilityLevel;
	}

}
