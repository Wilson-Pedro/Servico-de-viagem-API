package com.wamk.uber.integracao.exceptions;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.MotoristaNaoEncontradoException;
import com.wamk.uber.exceptions.TelefoneJaExisteException;
import com.wamk.uber.exceptions.UsuarioJaAtivoException;
import com.wamk.uber.exceptions.UsuarioJaDesativadoException;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.services.interfaces.UsuarioService;

@SpringBootTest
class UsuarioExceptionsTest {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioRepository usuarioRepository;

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
