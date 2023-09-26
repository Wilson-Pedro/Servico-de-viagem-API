package com.wamk.uber.enums.converter;

import java.util.stream.Stream;

import com.wamk.uber.enums.FormaDePagamento;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class FormaDePagamentoConverter implements AttributeConverter<FormaDePagamento, String>{

	@Override
	public String convertToDatabaseColumn(FormaDePagamento formaDePagamento) {
		if(formaDePagamento == null) {
			return null;
		}
		return formaDePagamento.getDescricao();
	}

	@Override
	public FormaDePagamento convertToEntityAttribute(String descricao) {
		if(descricao == null) {
			return null;
		}
		return Stream.of(FormaDePagamento.values())
				.filter(c -> c.getDescricao().equals(descricao))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
