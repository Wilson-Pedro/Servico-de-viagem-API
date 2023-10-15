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
import org.mockito.junit.jupiter.MockitoExtension;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	
	@InjectMocks
	UsuarioService usuarioService;
	
	@Mock
	UsuarioRepository usuarioRepository;
	
	Carro carro;
	
	Usuario passageiro;
	
	Usuario motorista;
	
	List<Usuario> usuarios = new ArrayList<>();
	
	@BeforeEach
	public void setUp() {
		carro = new Carro(1L, "Fiat", 2022, "JVF-9207");
		passageiro = new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
		motorista = new Motorista(2L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO, carro);
	
		usuarios.add(passageiro);
		usuarios.add(motorista);
	}
	
	@Test
	void deveSalvarUsuarioComSucesso() {
		UsuarioDTO usuarioDTO = new UsuarioDTO(passageiro);
		when(this.usuarioRepository.save(passageiro)).thenReturn(passageiro);
		Usuario user = usuarioService.save(usuarioDTO);
		assertEquals(passageiro, user);
	}
	
	@Test
	void deveBuscarTodosOsUsuariosComSucesso() {
		when(this.usuarioRepository.findAll()).thenReturn(usuarios);
		List<Usuario> list = this.usuarioService.findAll();
		assertEquals(usuarios, list);
	}
	
	@Test
	void deveBuscarUsuarioPorId() {
		when(this.usuarioRepository.findById(motorista.getId()))
		.thenReturn(Optional.of(motorista));
		Usuario user = this.usuarioService.findById(motorista.getId());
		assertEquals(motorista, user);
	}

	@Test
	void deveAtualizarUsuarioComSucesso() {
		when(this.usuarioRepository.save(passageiro)).thenReturn(passageiro);
		passageiro.setNome("Luffy");
		UsuarioDTO userDTO = new UsuarioDTO(passageiro);
		Usuario userUpdated = this.usuarioService.save(userDTO);
		assertEquals("Luffy", userUpdated.getNome());
	}
	
	@Test
	void deveDeletarUsuarioComSucesso() {
		this.usuarioRepository.delete(motorista);
		verify(this.usuarioRepository).delete(motorista);
	}
}