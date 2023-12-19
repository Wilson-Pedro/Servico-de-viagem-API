package com.wamk.uber.provider;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;

public class ViagemProviderTest implements ArgumentsProvider{

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		
		final var passageiro = new Passageiro(null, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
		
		final var motorista = new Motorista(null, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO);
		
		final var carro = new Carro(null, "Fiat", 2022, "JVF-9207", motorista);
		
		return Stream.of(Arguments.of(List.of(
				new Viagem(1L, 
						"Novo Castelo - Rua das Goiabas 1010", 
						"Pará - Rua das Maçãs", 
						"20min", passageiro, motorista, 
						FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA))
				));
	}
}
