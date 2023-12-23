package com.wamk.uber.dtos.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wamk.uber.dtos.CarroDTO;
import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.ViagemDTO;
import com.wamk.uber.dtos.input.ViagemInputDTO;
import com.wamk.uber.entities.Carro;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.FormaDePagamento;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.ViagemStatus;

@Component
public class MyObjectMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	public <T,R> R converter(T objetoASerConvertido, Class<R> classeAlvo) {
		return modelMapper.map(objetoASerConvertido, classeAlvo);
	}
	
	public <T,R> List<R> converterList(List<T> listaASerConvertida, Class<R> classeAlvo) {
		List<R> lista = listaASerConvertida
				.stream()
				.map(obj -> modelMapper.map(obj, classeAlvo))
				.collect(Collectors.toList());
		return lista;
	}
	
	// USUÁRIO
	public UsuarioDTO toUsuarioDTO(Usuario usuario) {
		if(usuario == null) {
			return null;
		}
		return new UsuarioDTO(usuario);
	}
	
	public Usuario toUsuarioEntity(UsuarioDTO usuarioDTO) {
		if(usuarioDTO == null) {
			return null;
		}
		var usuario = new Usuario();
		if(usuarioDTO.getId() != null) {
			usuario.setId(usuarioDTO.getId());
		}
		usuario.setNome(usuarioDTO.getNome());
		usuario.setTelefone(usuarioDTO.getTelefone());
		usuario.setTipoUsuario(TipoUsuario.toEnum(usuarioDTO.getTipoUsuario()));
		usuario.ativar();
		var user = passageiroOuMotorista(usuario);
		return user;
	}
	
	private Usuario passageiroOuMotorista(Usuario usuario) {
		if(usuario.getTipoUsuario().equals(TipoUsuario.PASSAGEIRO)) {
			Passageiro user = new Passageiro();
			BeanUtils.copyProperties(usuario, user);
			return user;
		} else {
			Motorista user = new Motorista();
			BeanUtils.copyProperties(usuario, user);
			return user;
		}
	}
	
	//VIAGEM
	public ViagemDTO toViagemDTO(Viagem viagem) {
		if(viagem == null) {
			return null;
		}
		return new ViagemDTO(viagem);
	}
	
	public Viagem toViagemEntity(ViagemInputDTO viagemInputDTO, Passageiro passageiro, Motorista motorista) {
		if(viagemInputDTO == null) {
			return null;
		}
		var viagem = new Viagem();
		if(viagemInputDTO.getId() != null) {
			viagem.setId(viagemInputDTO.getId());
		}
		
		viagem.setOrigem(viagemInputDTO.getOrigem());
		viagem.setDestino(viagemInputDTO.getDestino());
		viagem.setTempoDeViagem(viagemInputDTO.getTempoDeViagem());
		viagem.setPassageiro((Passageiro) passageiro);
		viagem.setMotorista((Motorista) motorista);
		viagem.setFormaDePagamento(FormaDePagamento.toEnum(viagemInputDTO.getFormaDePagamento()));
		viagem.setViagemStatus(ViagemStatus.NAO_FINALIZADA);
		return viagem;
	}
}
