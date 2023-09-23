package com.wamk.uber.enums.converter;

import java.util.stream.Stream;

import com.wamk.uber.enums.UsuarioStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UsuarioStatusConverter implements AttributeConverter<UsuarioStatus, String>{

	@Override
	public String convertToDatabaseColumn(UsuarioStatus usuarioStatus) {
		if(usuarioStatus == null) {
			return null;
		}
		return usuarioStatus.getDescricao();
	}

	@Override
	public UsuarioStatus convertToEntityAttribute(String descricao) {
		if(descricao == null) {
			return null;
		}
		return Stream.of(UsuarioStatus.values())
				.filter(c -> c.getDescricao().equals(descricao))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
