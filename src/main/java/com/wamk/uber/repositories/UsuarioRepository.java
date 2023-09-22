package com.wamk.uber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.UsuarioStatus;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	boolean existsByTelefone(String telefone);
	
	Motorista findByUsuarioStatus(UsuarioStatus status);
}
