package com.wamk.uber.controllers;

import com.wamk.uber.dtos.SolicitarViagemDTO;
import com.wamk.uber.dtos.UsuarioDTO;
import com.wamk.uber.dtos.mapper.UsuarioMapper;
import com.wamk.uber.entities.Usuario;
import com.wamk.uber.services.UsuarioService;
import com.wamk.uber.services.ViagemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final ViagemService viagemService;

    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, ViagemService viagemService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.viagemService = viagemService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<Usuario> list = usuarioService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(list.stream().map(usuarioMapper::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
        var usuario = usuarioService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioMapper.toDTO(usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> registrarUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        var usuario = usuarioService.save(usuarioDTO);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioMapper.toDTO(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualiziar(@RequestBody @Valid UsuarioDTO usuarioDTO,
                                                 @PathVariable Long id) {
        var usuario = usuarioService.atualizarCadastro(usuarioDTO, id);
        return ResponseEntity.status(HttpStatus.OK).body(usuarioMapper.toDTO(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        usuarioService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/solicitacao")
    public ResponseEntity<Object> solicitandoViagem(@RequestBody @Valid SolicitarViagemDTO solicitacao) {
        viagemService.solicitandoViagem(solicitacao);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
}
