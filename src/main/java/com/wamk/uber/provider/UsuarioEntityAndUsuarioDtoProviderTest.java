package com.wamk.uber.provider;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;

public class UsuarioEntityAndUsuarioDtoProviderTest implements ArgumentsProvider{

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		
		final var passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
		
		return Stream.of(Arguments.of(new UsuarioDTO(passageiro), passageiro));
	}
}
