package com.wamk.uber.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wamk.uber.dtos.UsuarioDTO;
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
	
	Passageiro p1;
	Passageiro p2;
	Passageiro p3;
	
	List<Usuario> usuarios = new ArrayList<>();
	
	@BeforeEach
	public void setUp() {
		p1 = new Passageiro(null, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO);
		p2 = new Passageiro(null, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		p3 = new Passageiro(null, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
	
		usuarios.add(p1);
		usuarios.add(p2);
		usuarios.add(p3);
	}
	
	@Test
	void deveSalvarUsuarioComSucesso() {
		UsuarioDTO usuarioDTO = new UsuarioDTO(p1);
		when(usuarioRepository.save(p1)).thenReturn(p1);
		Usuario user = usuarioService.save(usuarioDTO);
		assertEquals(p1, user);
	}

}