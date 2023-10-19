package com.wamk.uber.provider;

import com.wamk.uber.entities.Carro;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.stream.Stream;

public class CarrosProviderTest implements ArgumentsProvider{

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		
		return Stream.of(Arguments.of(List.of(
				new Carro(1L, "Fiat", 2022, "JVF-9207"),
				new Carro(2L, "Chevrolet", 2022, "FFG-0460"),
				new Carro(3L, "Forger", 2022, "FTG-0160"))
		));
	}
	
}
