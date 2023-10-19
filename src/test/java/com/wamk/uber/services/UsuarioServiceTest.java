package com.wamk.uber.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.provider.UsuarioEntityAndUsuarioDtoProviderTest;
import com.wamk.uber.provider.UsuarioProviderTest;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
	
	private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
	
	private final ViagemRepository viagemRepository = mock(ViagemRepository.class);
	
	private final UsuarioService usuarioService = new UsuarioService(usuarioRepository, viagemRepository);
	
	private final List<Usuario> usuarios = List.of(
			new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO),
			new Passageiro(2L, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Passageiro(3L, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO)
	);
	
	@Test
	void deveSalvarUsuarioComSucesso_usando_variavel_de_classe() {
		
		final var usuarioEsperado = usuarios.get(0);
		final var usuarioDTO = new UsuarioDTO(usuarioEsperado);
		
		when(usuarioRepository.save(usuarioEsperado)).thenReturn(usuarioEsperado);
		
		final var usuarioSalvo = usuarioService.save(usuarioDTO);
		
		assertThat(usuarioSalvo).usingRecursiveComparison().isEqualTo(usuarioEsperado);
	}
	
	@ParameterizedTest
	@ArgumentsSource(UsuarioEntityAndUsuarioDtoProviderTest.class)
	void deveSalvarUsuarioComSucesso_usando_teste_como_parametro(final UsuarioDTO usuarioDTO, 
			final Usuario usuarioEsperado) {
		
		when(usuarioRepository.save(usuarioEsperado)).thenReturn(usuarioEsperado);
		
		final var usuarioSalvo = usuarioService.save(usuarioDTO);
		
		assertThat(usuarioSalvo).usingRecursiveComparison().isEqualTo(usuarioEsperado);
	}
	
	@Test
	void deveBuscarTodosOsUsuariosComSucesso_usando_variavel_de_ambiente() {
		
		when(usuarioRepository.findAll()).thenReturn(usuarios);
		
		final var users = usuarioService.findAll();
		
		assertThat(users).usingRecursiveComparison().isEqualTo(usuarios);
	}
	
	@ParameterizedTest
	@ArgumentsSource(UsuarioProviderTest.class)
	void deveBuscarTodosOsUsuariosComSucesso_usando_teste_como_parametro(
			List<Usuario> usuariosEsperados) {
		
		when(usuarioRepository.findAll()).thenReturn(usuariosEsperados);
		
		final var users = usuarioRepository.findAll();
		
		assertThat(users).usingRecursiveComparison().isEqualTo(usuarios);
	}

	@Test
	void deveBuscarUsuarioPorId() {
		
		final var usuarioEsperado = usuarios.get(0);
		
		when(usuarioRepository.findById(usuarioEsperado.getId()))
				.thenReturn(Optional.of(usuarioEsperado));
		
		final  var user = usuarioService.findById(usuarioEsperado.getId());
		
		assertThat(user).usingRecursiveComparison().isEqualTo(usuarioEsperado);
	}

	@Test
	void deveAtualizarUsuarioComSucesso() {
		
		final var usuarioEsperado = usuarios.get(0);
		
		when(usuarioRepository.save(usuarioEsperado)).thenReturn(usuarioEsperado);
		
		assertNotEquals("Lucas", usuarioEsperado.getNome());
		
		usuarioEsperado.setNome("Lucas");
		
		UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioEsperado);
		final var usuarioAtualizado = usuarioService.save(usuarioDTO);
		
		assertEquals("Lucas", usuarioAtualizado.getNome());
	}

	@Test
	void deveDeletarUsuarioComSucesso() {
		
		final var usuarioEsperado = usuarios.get(0);
		
		when(usuarioRepository.findById(usuarioEsperado.getId()))
				.thenReturn(Optional.of(usuarioEsperado));
		
		doNothing().when(usuarioRepository).delete(usuarioEsperado);
		
		usuarioRepository.delete(usuarioEsperado);
		
		verify(usuarioRepository, times(1)).delete(usuarioEsperado);
	}
}