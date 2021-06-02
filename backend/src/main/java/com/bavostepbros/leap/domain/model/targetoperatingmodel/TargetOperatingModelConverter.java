package com.bavostepbros.leap.domain.model.targetoperatingmodel;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TargetOperatingModelConverter implements AttributeConverter<TargetOperatingModel, Integer> {

	@Override
	public Integer convertToDatabaseColumn(TargetOperatingModel attribute) {
		if (attribute == null) {
			return null;
		}
		return attribute.getTargetOperatingModelValue();
	}

	@Override
	public TargetOperatingModel convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}
		
		TargetOperatingModel targetOperatingModel = Stream.of(TargetOperatingModel.values())
				.filter(t -> t.getTargetOperatingModelValue().equals(dbData))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		return targetOperatingModel;
	}

}
