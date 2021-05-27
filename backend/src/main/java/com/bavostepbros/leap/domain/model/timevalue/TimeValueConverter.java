package com.bavostepbros.leap.domain.model.timevalue;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TimeValueConverter implements AttributeConverter<TimeValue, Integer> {

	@Override
	public Integer convertToDatabaseColumn(TimeValue attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.getValue();
	}

	@Override
	public TimeValue convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}
		
		TimeValue timeValue = Stream.of(TimeValue.values())
				.filter(t -> t.getValue().equals(dbData))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		return timeValue;
	}

}
