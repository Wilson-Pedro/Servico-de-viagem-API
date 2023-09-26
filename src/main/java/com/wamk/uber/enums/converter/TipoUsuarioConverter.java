package com.wamk.uber.enums.converter;

import java.util.stream.Stream;

import com.wamk.uber.enums.TipoUsuario;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoUsuarioConverter implements AttributeConverter<TipoUsuario, String>{

	@Override
	public String convertToDatabaseColumn(TipoUsuario tipoUsuario) {
		if(tipoUsuario == null) {
			return null;
		}
		return tipoUsuario.getDescricao();
	}

	@Override
	public TipoUsuario convertToEntityAttribute(String descricao) {
		if(descricao == null) {
			return null;
		}
		return Stream.of(TipoUsuario.values())
				.filter(c -> c.getDescricao().equals(descricao))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}
