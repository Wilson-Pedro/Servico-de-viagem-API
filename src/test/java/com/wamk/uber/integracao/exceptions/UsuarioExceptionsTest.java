package com.wamk.uber.integracao.exceptions;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.MotoristaNaoEncontradoException;
import com.wamk.uber.exceptions.TelefoneJaExisteException;
import com.wamk.uber.exceptions.UsuarioJaAtivoException;
import com.wamk.uber.exceptions.UsuarioJaDesativadoException;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.services.UsuarioService;

@SpringBootTest
class UsuarioExceptionsTest {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	private final List<Usuario> usuarios = List.of(
			new Passageiro(1L, "Wilson", "9816923456", TipoUsuario.PASSAGEIRO, UsuarioStatus.CORRENDO),
			new Passageiro(2L, "Ana", "983819-2470", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Passageiro(3L, "Luan", "983844-2479", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO),
			new Motorista(4L, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.CORRENDO),
			new Motorista(5L, "Julia", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO),
			new Motorista(6L, "Carla", "9833163865", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO)
	);

	@Test
	@DisplayName("Deve lançar exceção: EntidadeNaoEncontradaException")
	void deveLancarExcecaoAposTentarBuscarUmUsuarioInexistente() {
		
		Long id = 40L;
		
		assertThrows(EntidadeNaoEncontradaException.class, 
				() -> this.usuarioService.findById(id));
	}

	@Test
	@DisplayName("Deve lançar exceção: MotoristaNaoEncontradoException")
	void deveLancarExcecaoAposNaoEncontrarMototistaComStatusPassadoNoParametro() {
		
		UsuarioStatus desativado = UsuarioStatus.DESATIVADO;
		
		assertThrows(MotoristaNaoEncontradoException.class, 
				() -> this.usuarioService.buscarMotoristaPorStatus(desativado));
		
	}
	
	@Test
	@DisplayName("Deve lançar exceção: TelefoneJaExisteException")
	void deveLancarExcecaoAposTentarRegistrarUmUsuario() {
		
		final var usuarioSalvo = new Passageiro(null, "Luan", "981441279", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		this.usuarioRepository.save(usuarioSalvo);
		
		final var usuario = new Passageiro(null, "Jana", "981441279", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		final var usuarioDto = new UsuarioDTO(usuario);
		
		assertThrows(TelefoneJaExisteException.class, 
				() -> this.usuarioService.save(usuarioDto));
		
	}
	
	@Test
	@DisplayName("Deve lançar exceção: UsuarioJaDesativadoException, após tentar desativar um usuario já desativado.")
	void deveLancarExcecaoAposTentarDesativarUmUsuario() {
		
		final var usuario = new Passageiro(null, "Luan", "221441179", TipoUsuario.PASSAGEIRO, UsuarioStatus.DESATIVADO);
		
//		final var usuarioDto = new UsuarioDTO(usuario);
		this.usuarioRepository.save(usuario);
		
		Long id = usuario.getId();
		
		assertThrows(UsuarioJaDesativadoException.class, 
				() -> this.usuarioService.desativarUsuario(id));
	} 
	
	@Test
	@DisplayName("Deve lançar exceção: UsuarioJaAtivoException, após tentar ativado um usuario já ativado.")
	void deveLancarExcecaoAposTentarAtivarUmUsuario() {
		
		Passageiro passageiro = new Passageiro(null, "Ana", "7715328-1472", TipoUsuario.PASSAGEIRO, UsuarioStatus.ATIVO);
		
		usuarioRepository.save(passageiro);
		Long id = passageiro.getId();
		
		assertThrows(UsuarioJaAtivoException.class, 
				() -> this.usuarioService.ativarUsuario(id));
	}
}
