package com.wamk.uber.services;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.mapper.UsuarioMapper;
import com.wamk.uber.entities.Motorista;
import com.wamk.uber.entities.Passageiro;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.entities.Viagem;
import com.wamk.uber.enums.TipoUsuario;
import com.wamk.uber.enums.UsuarioStatus;
import com.wamk.uber.exceptions.EntidadeNaoEncontradaException;
import com.wamk.uber.exceptions.MotoristaNaoEncontradoException;
import com.wamk.uber.exceptions.TelefoneExistenteException;
import com.wamk.uber.repositories.UsuarioRepository;
import com.wamk.uber.repositories.ViagemRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	
	private final ViagemRepository viagemRepository;

	private final UsuarioMapper usuarioMapper;
	
	public UsuarioService(UsuarioRepository usuarioRepository, ViagemRepository viagemRepository, UsuarioMapper usuarioMapper) {
		this.usuarioRepository = usuarioRepository;
		this.viagemRepository = viagemRepository;
		this.usuarioMapper = usuarioMapper;
	}

	@Transactional
	public Usuario save(UsuarioDTO usuarioDTO) {
		validarCadastroUsuario(usuarioDTO);
		return usuarioRepository.save(usuarioMapper.toEntity(usuarioDTO));
	}

	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}
	
	public Page<Usuario> findAll(Pageable pageable) {
		return usuarioRepository.findAll(pageable);
	}

	public Usuario findById(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}
	
	public Motorista findByMotoristaStatus(UsuarioStatus status) {
		List<Motorista> list = usuarioRepository.findAllByUsuarioStatus(status);
		return list.stream()
				.filter(x -> x.getUsuarioStatus().equals(status))
				.findFirst()
				.orElseThrow(() -> new MotoristaNaoEncontradoException(""));
	}

	public Usuario atualizarCadastro(UsuarioDTO usuarioDTO, Long id) {
		validarUpdateUsuario(usuarioDTO, id);
		return usuarioRepository.findById(id)
				.map(usuario -> {
					usuario.setNome(usuarioDTO.getNome());
					usuario.setTelefone(usuarioDTO.getTelefone());
					usuario.setTipoUsuario(TipoUsuario.toEnum(usuarioDTO.getTipoUsuario()));
					return usuarioRepository.save(usuario);
				}).orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada."));
	}
	
	@Transactional
	public void delete(Long id) {
		usuarioRepository.delete(usuarioRepository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Entidade não encontrada.")));
	}
	
	public void validarCadastroUsuario(UsuarioDTO usuarioDTO) {
		if(usuarioRepository.existsByTelefone(usuarioDTO.getTelefone())) {
			throw new TelefoneExistenteException("");
		}
	}
	
	public void validarUpdateUsuario(UsuarioDTO usuarioDTO, Long id) {
		if(usuarioRepository.existsByTelefone(usuarioDTO.getTelefone()) 
				&& !Objects.equals(usuarioDTO.getId(), id)) {
			throw new TelefoneExistenteException("");
		}
	}
	
	public void activateUserByViagemId(Long id) {
		Viagem viagem = viagemRepository.findById(id).get();
		Passageiro passageiro = (Passageiro) findById(viagem.getPassageiro().getId());
		Motorista motorista = (Motorista) findById(viagem.getMotorista().getId());
		passageiro.setUsuarioStatus(UsuarioStatus.ATIVO);
		motorista.setUsuarioStatus(UsuarioStatus.ATIVO);
		usuarioRepository.saveAll(List.of(passageiro, motorista));
	}

	public void desativarUsuario(Long id) {
		var usuario = findById(id);
		usuario.setUsuarioStatus(UsuarioStatus.DESATIVADO);
		usuarioRepository.save(usuario);
	}

	public void ativarUsuario(Long id) {
		var usuario = findById(id);
		usuario.setUsuarioStatus(UsuarioStatus.ATIVO);
		usuarioRepository.save(usuario);
		
	}
}