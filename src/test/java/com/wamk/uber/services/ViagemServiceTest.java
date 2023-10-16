package com.wamk.uber.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.dtos.mapper.ViagemMapper;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;

@ExtendWith(MockitoExtension.class)
class ViagemServiceTest {
	
	@InjectMocks
	ViagemService viagemService;
	
	@Mock
	ViagemRepository viagemRepository;
	
	@Mock
	UsuarioRepository usuarioRepository;
	
	@Mock
	UsuarioService usuarioService;
	
	@Spy
	ViagemMapper viagemMapper;
	
	Carro carro;
	
	Usuario passageiro;
	
	Usuario motorista;
	
	Viagem viagem;
	
	List<Viagem> viagens = new ArrayList<>();

	@BeforeEach
	void test() {
		carro = new Carro(1L, "Fiat", 2022, "JVF-9207");
		
		passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
		
		motorista = new Motorista(2L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO, carro);
		
		viagem = new Viagem(1L, 
				"Novo Castelo - Rua das Goiabas 1010", "Pará - Rua das Maçãs", 
				"20min",(Passageiro) passageiro,(Motorista) motorista, 
				FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA);
		viagens.add(viagem);
		
		viagemMapper = new ViagemMapper();
		
		usuarioService = new UsuarioService(usuarioRepository, viagemRepository);
	}
	
	@Test
	void deveSalvarViagemComSucesso() { 
		ViagemInputDTO viagemDTO = new ViagemInputDTO(viagem);
		when(this.viagemRepository.save(viagem)).thenReturn(viagem);
		Viagem trip = viagemService.save(viagemDTO);
		assertEquals(viagem, trip);
	}
	
	@Test
	void deveBuscarTodasAsViagensComSucesso() {
		when(this.viagemRepository.findAll()).thenReturn(viagens);
		List<Viagem> list = this.viagemService.findAll();
		assertEquals(viagens, list);
	}
	
	@Test
	void deveBuscarViagemPorIdComSucesso() {
		when(this.viagemRepository.findById(viagem.getId())).thenReturn(Optional.of(viagem));
		Viagem trip = this.viagemService.findById(viagem.getId());
		assertEquals(viagem, trip);
	}
	
	@Test
	void deveAtualizarViagemComSucesso() {
		when(this.viagemRepository.save(viagem)).thenReturn(viagem);
		viagem.setFormaDePagamento(FormaDePagamento.DEBITO);
		ViagemInputDTO viagemDTO = new ViagemInputDTO(viagem);
		Viagem viagemAtualizada = this.viagemService.save(viagemDTO);
		assertEquals(FormaDePagamento.DEBITO, viagemAtualizada.getFormaDePagamento());
	}
	
	@Test
	void deveDeletarViagemComSucesso() {
		this.viagemRepository.delete(viagem);
		verify(viagemRepository).delete(viagem);
	}
}
