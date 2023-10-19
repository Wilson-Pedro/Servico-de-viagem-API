package com.wamk.uber.provider;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.entities.Carro;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class CarroEntityAndCarroDtoProviderTest implements ArgumentsProvider{

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		
		final var carro = new Carro(1L, "Fiat", 2022, "JVF-9207");
		
		return Stream.of(Arguments.of(new CarroDTO(carro), carro));
	}
}
