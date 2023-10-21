package com.wamk.uber.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wamk.uber.entities.Carro;

public interface CarroRepository extends JpaRepository<Carro, Long>{

	boolean existsByPlaca(String placa);
	
	Optional<Carro> findCarroByPlaca(String placa);
}
