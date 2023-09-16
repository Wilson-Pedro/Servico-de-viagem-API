package com.wamk.uber.dtos;

import org.hibernate.validator.constraints.Length;

import com.wamk.uber.entities.Usuario;
import com.wamk.uber.enums.TipoUsuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

	private Long id;
	
	@NotNull
	@NotBlank
	@Length(min = 2, max = 100)
	private String nome;
	
	@NotNull
	@NotBlank
	@Length(min = 5, max = 100)
	private String telefone;
	
	@NotNull
	private TipoUsuario tipoUsuario;
	
	public UsuarioDTO(Usuario usuario) {
		id = usuario.getId();
		nome = usuario.getNome();
		telefone = usuario.getTelefone();
		tipoUsuario = usuario.getTipoUsuario();
	}
}
