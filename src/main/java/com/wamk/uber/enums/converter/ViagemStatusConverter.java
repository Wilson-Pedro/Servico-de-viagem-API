package com.wamk.uber.enums.converter;

import com.wamk.uber.enums.ViagemStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ViagemStatusConverter implements AttributeConverter<ViagemStatus, String>{

	@Override
	public String convertToDatabaseColumn(ViagemStatus viagemStatus) {
		if(viagemStatus == null) {
			return null;
		}
		return viagemStatus.getDescricao();
	}

	@Override
	public ViagemStatus convertToEntityAttribute(String descricao) {
		if(descricao == null) {
			return null;
		}
		return ViagemStatus.toEnum(descricao);
	}

}
