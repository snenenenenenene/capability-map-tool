package com.bavostepbros.leap.domain.model.strategicimportance;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StrategicImportanceConverter implements AttributeConverter<StrategicImportance, Integer> {

	@Override
	public Integer convertToDatabaseColumn(StrategicImportance attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.getImportance();
	}

	@Override
	public StrategicImportance convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}
		
		StrategicImportance strategicImportance = Stream.of(StrategicImportance.values())
				.filter(s -> s.getImportance().equals(dbData))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		return strategicImportance;
	}
	
}
