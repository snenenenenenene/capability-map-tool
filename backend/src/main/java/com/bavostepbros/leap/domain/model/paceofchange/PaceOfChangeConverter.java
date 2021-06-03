package com.bavostepbros.leap.domain.model.paceofchange;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PaceOfChangeConverter implements AttributeConverter<PaceOfChange, Integer> {

	@Override
	public Integer convertToDatabaseColumn(PaceOfChange attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.getPaceOfChangeValue();
	}

	@Override
	public PaceOfChange convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}
		
		PaceOfChange paceOfChange = Stream.of(PaceOfChange.values())
				.filter(p -> p.getPaceOfChangeValue().equals(dbData))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		return paceOfChange;
	}

}
