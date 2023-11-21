package com.wamk.uber.exceptions;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;
import com.wamk.uber.services.ViagemService;

@SpringBootTest
class ViagensExceeptions {
	
	@Autowired
	ViagemService viagemService;
	
	@Autowired
	ViagemRepository viagemRepository;
	
	@Autowired
	UsuarioRepository usuarioRepositoryr;
	
	Carro carro = new Carro(1L, "Fiat", 2022, "JVF-9207");
	Carro carro2 = new Carro(2L, "Chevrolet", 2022, "FFG-0460");
	
	Passageiro passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
	Passageiro passageiro2 = new Passageiro(2L, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
	
	Motorista motorista = new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO, carro);
	Motorista motorista2 = new Motorista(5L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO, carro2);
	
	private final List<Viagem> viagens = List.of(
			new Viagem(1L, 
					"Novo Castelo - Rua das Goiabas 1010", 
					"Pará - Rua das Maçãs", 
					"10 minutos", passageiro, motorista, 
					FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA),
			new Viagem(2L, 
					"Novo Castelo - Rua das Limonadas 1020", 
					"Pará - Rua das Peras", 
					"20 minutos", passageiro2, motorista2, 
					FormaDePagamento.DEBITO, ViagemStatus.FINALIZADA)
	);

	@Test
	@DisplayName("Deve lançar Exceção: EntidadeNaoEncontradaException")
	void deveLancarExcecaoAposTentarBuscarViagemInexistente() {
		
		Long id = 10L;
		
		assertThrows(EntidadeNaoEncontradaException.class, 
				() -> this.viagemService.findById(id));
	}

	@Test
	@DisplayName("Deve lançar Exceção: PassageiroCorrendoException, após tentar montar uma "
			+ "viagem com um passageiro que já está correndo(em viagem)")
	void deveLancarExcecaoAposTentarSolicitarUmaViagem() {
		
		SolicitarViagemDTO solicitacao = new SolicitarViagemDTO(viagens.get(0));
		
		assertThrows(PassageiroCorrendoException.class,
				() -> this.viagemService.solicitandoViagem(solicitacao));
	}
	
	@Test
	@DisplayName("Deve lançar Exceção: UsuarioDesativadoException, após tentar montar uma "
			+ "viagem com um usuário que está desativado")
	void deveLancarExcecaoAposTentarSolicitarUmaViagemUmUsuarioDesativado() {
		
		Passageiro passageiro = new Passageiro(3L, "Ana", "8824719-2070", TipoUsuario.PASSAGEIRO, UsuarioStatus.DESATIVADO);
		
		usuarioRepositoryr.save(passageiro);
		
		SolicitarViagemDTO solicitacao = new SolicitarViagemDTO();
		solicitacao.setPassageiroId(passageiro.getId());
		solicitacao.setOrigem("Novo Castelo - Rua das Limonadas 1020");
		solicitacao.setDestino("Pará - Rua das Maçãs");
		solicitacao.setDestino("500m");
		solicitacao.setFormaDePagamento(FormaDePagamento.PIX);
		
		assertThrows(UsuarioDesativadoException.class,
				() -> this.viagemService.solicitandoViagem(solicitacao));
	}
	
	@Test
	@DisplayName("Deve lançar Exceção: ViagemJaFinalizadaException")
	void deveLancarExcecaoAposCancelarUmaViagem() {
		
		viagemRepository.save(viagens.get(1));
		
		Long id = viagens.get(1).getId();
		
		assertThrows(ViagemJaFinalizadaException.class, 
				() -> this.viagemService.cancelarViagemPorViagemId(id));
	}
}
