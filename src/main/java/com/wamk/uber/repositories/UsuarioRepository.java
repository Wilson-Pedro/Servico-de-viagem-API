package com.wamk.uber.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.UsuarioStatus;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	boolean existsByTelefone(String telefone);
	
	Usuario findByUsuarioStatus(UsuarioStatus status);
	
	List<Motorista> findAllByUsuarioStatus(UsuarioStatus status);
}
