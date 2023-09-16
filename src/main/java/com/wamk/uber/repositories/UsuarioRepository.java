package com.wamk.uber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wamk.uber.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	boolean existsByTelefone(String telefone);
}
