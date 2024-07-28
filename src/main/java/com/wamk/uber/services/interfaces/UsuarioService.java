package com.wamk.uber.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.UsuarioStatus;

public interface UsuarioService {

	Usuario save(UsuarioDTO usuarioDTO);
	
	List<Usuario> findAll();
	
	Page<Usuario> findAll(Pageable pageable);
	
	Usuario findById(Long id);
	
	Motorista buscarMotoristaPorStatus(UsuarioStatus status);
	
	Usuario atualizarCadastro(UsuarioDTO usuarioDTO, Long id);
	
	void delete(Long id);
	
	void validarCadastroUsuario(UsuarioDTO usuarioDTO);
	
	void validarAtualizacaoDoUsuario(UsuarioDTO usuarioDTO, Long id);
	
	void ativarUsuarioPorViagemId(Long id);
	
	void desativarUsuario(Long id);
	
	void ativarUsuario(Long id);
	
	void inserirNoBancoDeDados(Usuario usuario);
}
