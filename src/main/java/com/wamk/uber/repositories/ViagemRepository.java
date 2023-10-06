package com.wamk.uber.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;

public interface ViagemRepository extends JpaRepository<Viagem, Long>{

	Viagem findByPassageiro(Passageiro passageiro);
	
	Viagem findByMotorista(Motorista motorista);
	
	List<Viagem> findAllByPassageiro(Passageiro passageiro);
	
	List<Viagem> findAllByMotorista(Motorista motorista);
	
	boolean existsById(Long id);
}
