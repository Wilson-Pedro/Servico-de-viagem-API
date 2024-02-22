package com.wamk.uber.integracao.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.repositories.UsuarioRepository;

@DataJpaTest
//@ActiveProfiles("test")
class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository usuarioRepository;

	@Test
	@DisplayName("Deve confirmar a existência de um Usuário a partir de um número telefone com sucesso")
	void deveValidarSeExisteUsarioComDeterminadoTelefoneCase1() {
		usuarioRepository.deleteAll();
		usuarioRepository.save(new Motorista(null, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO));
		
		String telefone = "9822349876";
		
		boolean existe = usuarioRepository.existsByTelefone(telefone);
		
		assertTrue(existe);
	}

	@Test
	@DisplayName("Não deve confirmar a existência de um Usuário a partir de um número telefone com sucesso")
	void deveValidarSeExisteUsarioComDeterminadoTelefoneCase2() {
		String telefone = "2866958451";
		
		boolean existe = usuarioRepository.existsByTelefone(telefone);
		
		assertFalse(existe);
	}
	
	@Test
	@DisplayName("Deve buscar todos os motoristas a partir do UsuarioStatus com sucesso")
	void deveBuscarTodosOsMotoristasAPartirDoStatusComSucesso() {
		usuarioRepository.deleteAll();
		usuarioRepository.save(new Motorista(null, "Pedro", "9822349876", TipoUsuario.MOTORISTA, UsuarioStatus.ATIVO));
		
		UsuarioStatus ativo = UsuarioStatus.ATIVO;
		TipoUsuario motorista = TipoUsuario.MOTORISTA; 
		
		List<Usuario> motoristasEsperados = usuarioRepository.findAll()
				.stream()
				.filter(x -> x.getTipoUsuario().equals(motorista) && x.getUsuarioStatus().equals(ativo))
				.collect(Collectors.toList());
		
		List<Motorista> motoristas = usuarioRepository.findAllByUsuarioStatus(ativo);
		
		assertNotNull(motoristas);
		assertFalse(motoristas.isEmpty());
		assertThat(motoristas).usingRecursiveComparison().isEqualTo(motoristasEsperados);
	}
}
