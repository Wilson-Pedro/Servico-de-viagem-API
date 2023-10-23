package com.wamk.uber.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Motorista;
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
			new Passageiro(3L, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO, null),
			new Motorista(5L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO, null),
			new Motorista(6L, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO, null)
	);
	
	@Test
	void deveSalvarUsuarioComSucesso_usandoVariavelDeClasse() {
		
		final var usuarioEsperado = usuarios.get(0);
		final var usuarioDTO = new UsuarioDTO(usuarioEsperado);
		
		when(usuarioRepository.save(usuarioEsperado)).thenReturn(usuarioEsperado);
		
		final var usuarioSalvo = usuarioService.save(usuarioDTO);
		
		assertThat(usuarioSalvo).usingRecursiveComparison().isEqualTo(usuarioEsperado);
	}
	
	@ParameterizedTest
	@ArgumentsSource(UsuarioEntityAndUsuarioDtoProviderTest.class)
	void deveSalvarUsuarioComSucesso_usandoTesteComParametros(final UsuarioDTO usuarioDTO, 
			final Usuario usuarioEsperado) {
		
		when(usuarioRepository.save(usuarioEsperado)).thenReturn(usuarioEsperado);
		
		final var usuarioSalvo = usuarioService.save(usuarioDTO);
		
		assertThat(usuarioSalvo).usingRecursiveComparison().isEqualTo(usuarioEsperado);
	}
	
	@Test
	void deveBuscarTodosOsUsuariosComSucesso_usandoVariavelDeClasse() {
		
		when(usuarioRepository.findAll()).thenReturn(usuarios);
		
		final var users = usuarioService.findAll();
		
		assertThat(users).usingRecursiveComparison().isEqualTo(usuarios);
	}
	
	@ParameterizedTest
	@ArgumentsSource(UsuarioProviderTest.class)
	void deveBuscarTodosOsUsuariosComSucesso_usandoTesteComParametros(
			List<Usuario> usuariosEsperados) {
		
		when(usuarioRepository.findAll()).thenReturn(usuariosEsperados);
		
		final var users = usuarioService.findAll();
		
		assertThat(users).usingRecursiveComparison().isEqualTo(usuariosEsperados);
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
	
	@Test
	void deveAtivarUsuarioComSucesso() {
		
		final var usuarioEsperado = usuarios.get(0);
		UsuarioStatus ativo = UsuarioStatus.ATIVO;
		
		when(usuarioRepository.save(usuarioEsperado)).thenReturn(usuarioEsperado);
		
		usuarioEsperado.ativar();
		usuarioRepository.save(usuarioEsperado);
		
		verify(usuarioRepository, times(1)).save(usuarioEsperado);
		
		assertEquals(ativo, usuarioEsperado.getUsuarioStatus());
	}
	
	@Test
	void deveDesativarUsuarioComSucesso() {
		
		final var usuarioEsperado = usuarios.get(0);
		UsuarioStatus desativado = UsuarioStatus.DESATIVADO;
		
		when(usuarioRepository.save(usuarioEsperado)).thenReturn(usuarioEsperado);
		
		usuarioEsperado.setUsuarioStatus(desativado);
		usuarioRepository.save(usuarioEsperado);
		
		verify(usuarioRepository, times(1)).save(usuarioEsperado);
		
		assertEquals(desativado, usuarioEsperado.getUsuarioStatus());
	}
	
	@Test
	@DisplayName("Deve buscar o primeiro motorista que estiver com o status ATIVO")
	void deveBuscarMotoristaPorUsuarioStatus() {
		
		List<Motorista> motoristas = new ArrayList<>();
		
		motoristas.add((Motorista) usuarios.get(3));
		motoristas.add((Motorista) usuarios.get(4));
		motoristas.add((Motorista) usuarios.get(5));
		
		UsuarioStatus ativo = UsuarioStatus.ATIVO;
		
		Motorista motoristaEsperado = (Motorista) usuarios.get(4);
		
		when(usuarioRepository.findAllByUsuarioStatus(ativo)).thenReturn(motoristas);
		
		Motorista motorista = usuarioService.findMotoristaByStatus(ativo);
		
		assertThat(motorista).usingRecursiveComparison().isEqualTo(motoristaEsperado);
	}
}