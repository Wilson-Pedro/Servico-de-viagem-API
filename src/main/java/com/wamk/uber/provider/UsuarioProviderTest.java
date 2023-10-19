package com.wamk.uber.provider;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;

public class UsuarioProviderTest implements ArgumentsProvider{

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

		return Stream.of(Arguments.of(List.of(
				new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, 
						UsuarioStatus.CORRENDO),
				new Passageiro(2L, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, 
						UsuarioStatus.ATIVO),
				new Passageiro(3L, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, 
						UsuarioStatus.ATIVO))
		));
	}
}
