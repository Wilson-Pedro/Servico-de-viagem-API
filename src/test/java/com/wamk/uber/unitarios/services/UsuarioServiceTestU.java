package com.wamk.uber.unitarios.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.mapper.MyObjectMapper;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.enums.ViagemStatus;
import com.wamk.uber.exceptions.TelefoneJaExisteException;
import com.wamk.uber.exceptions.UsuarioJaAtivoException;
import com.wamk.uber.exceptions.UsuarioJaDesativadoException;
import com.wamk.uber.provider.UsuarioEntityAndUsuarioDtoProviderTest;
import com.wamk.uber.provider.UsuarioProviderTest;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;
import com.wamk.uber.services.UsuarioServiceImpl;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTestU {
	
	private final MyObjectMapper modelMapper = mock(MyObjectMapper.class);
	
	private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
	
	private final ViagemRepository viagemRepository = mock(ViagemRepository.class);
	
	private final UsuarioServiceImpl usuarioService = new UsuarioServiceImpl(modelMapper, usuarioRepository, viagemRepository);
	
	private final List<Usuario> usuarios = List.of(
			new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO),
			new Passageiro(2L, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Passageiro(3L, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO),
			new Motorista(5L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO),
			new Motorista(6L, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO)
	);
	
	final Viagem viagem = new Viagem(1L, "Novo Castelo - Rua das Goiabas 1010", 
			"Pará - Rua das Maçãs", "20min", 
			(Passageiro)usuarios.get(0), (Motorista) usuarios.get(3), 
			FormaDePagamento.PIX, ViagemStatus.NAO_FINALIZADA);
	
	@Test
	void deveSalvarUsuarioComSucesso_usandoVariavelDeClasse() {
		
		final var usuarioEsperado = usuarios.get(0);
		
		when(usuarioRepository.save(usuarioEsperado)).thenReturn(usuarioEsperado);
		
		final var usuarioSalvo = usuarioRepository.save(usuarioEsperado);
		
		assertThat(usuarioSalvo).usingRecursiveComparison().isEqualTo(usuarioEsperado);
	}
	
	@ParameterizedTest
	@ArgumentsSource(UsuarioEntityAndUsuarioDtoProviderTest.class)
	void deveSalvarUsuarioComSucesso_usandoTesteComParametros(final UsuarioDTO usuarioDTO, 
			final Usuario usuarioEsperado) {
		
		when(usuarioRepository.save(usuarioEsperado)).thenReturn(usuarioEsperado);
		
		final var usuarioSalvo = usuarioRepository.save(usuarioEsperado);
		
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
		
		assertNotEquals("Lucas", usuarioEsperado.getNome());
		
		usuarioEsperado.setNome("Lucas");
		
		when(usuarioRepository.save(usuarioEsperado)).thenReturn(usuarioEsperado);
		
		final var usuarioAtualizado = usuarioRepository.save(usuarioEsperado);
		
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
		
		Motorista motorista = usuarioService.buscarMotoristaPorStatus(ativo);
		
		assertThat(motorista).usingRecursiveComparison().isEqualTo(motoristaEsperado);
	}
	
	@Test
	void deveAtivarUsuarioPorViagemId() {
		
		final var trip = viagem;
		final var passageiro = viagem.getPassageiro();
		
		UsuarioStatus ativo = UsuarioStatus.ATIVO;
		
		when(viagemRepository.findById(trip.getId())).thenReturn(Optional.of(trip));
		when(usuarioRepository.save(passageiro)).thenReturn(passageiro);
		
		passageiro.ativar();
		usuarioRepository.save(passageiro);
		
		assertEquals(ativo, passageiro.getUsuarioStatus());
	}
	
	@Test
	void deveLancarExcecaoTelefoneJaExisteException() {
		
		final var userDto = new UsuarioDTO(usuarios.get(0));
		final var telefone = userDto.getTelefone();
		when(this.usuarioRepository.existsByTelefone(telefone)).thenReturn(true);
		
		assertThrows(TelefoneJaExisteException.class, 
				() -> usuarioService.validarCadastroUsuario(userDto));
	}
	
	@Test
	void deveLancarExcecaoUsuarioJaDesativadoException() {
		final var user = usuarios.get(1);
		Long id = user.getId();
		user.desativar();
		
		when(this.usuarioRepository.findById(id)).thenReturn(Optional.of(user));
		when(this.usuarioRepository.save(user)).thenReturn(user);
		
		this.usuarioRepository.save(user);
		
		final var userFind = this.usuarioRepository.findById(id).get();
		
		assertThrows(UsuarioJaDesativadoException.class, () -> usuarioService.desativarUsuario(userFind.getId()));
	}
	
	@Test
	void deveLancarExcecaoUsuarioJaAtivoException() {
		final var user = usuarios.get(1);
		Long id = user.getId();
		
		when(this.usuarioRepository.findById(id)).thenReturn(Optional.of(user));
		when(this.usuarioRepository.save(user)).thenReturn(user);
		
		this.usuarioRepository.save(user);
		
		final var userFind = this.usuarioRepository.findById(id).get();
		
		assertThrows(UsuarioJaAtivoException.class, () -> usuarioService.ativarUsuario(userFind.getId()));
	}
	
	@Test
	void devePaginarUmaListaDeCarrosComSucesso() {
		
		List<Usuario> list = this.usuarios;
		Page<Usuario> page = new PageImpl<>(list, PageRequest.of(0, 10), list.size());
		
		when(usuarioService.findAll(any(Pageable.class))).thenReturn(page);
		
		Page<Usuario> pageUsuario = usuarioService.findAll(PageRequest.of(0, 10));
		
		assertNotNull(pageUsuario);
		assertEquals(pageUsuario.getContent().size(), list.size());
	}
}