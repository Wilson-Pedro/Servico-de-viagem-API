package com.wamk.uber.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Viagem;

public interface ViagemService {

	Viagem save(ViagemInputDTO viagemInputDTO);
	
	Viagem findById(Long id);
	
	Viagem acharViagemPorUserId(Long UserId);
	
	Viagem acharViagemPorPassageiro(Passageiro passageiro);
	
	Viagem acharViagemPorMotorista(Motorista motorista);
	
	Viagem atualizarCadastro(ViagemInputDTO viagemInputDTO, Long id);
	
	Viagem construirViagem(SolicitarViagemDTO solicitacao);
	
	List<Viagem> buscarTodasAsViagensPorUserId(Long UserId);
	
	List<Viagem> acharTodasAsViagensPorUserId(Long UserId);
	
	List<Viagem> findAll();
	
	Page<Viagem> findAll(Pageable pageable);
	
	void delete(Long id);
	
	void solicitandoViagem(SolicitarViagemDTO solicitacao);
	
	void validarSolicitagem(Passageiro passageiro);
	
	void finalizarViagem(Long id);
	
	void cancelarViagemPorUserId(Long Userid);
	
	void cancelarViagemPorViagemId(Long id);
	
}
