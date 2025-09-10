package com.victormacedov.library_api.controller;

import com.victormacedov.library_api.controller.dto.UsuarioDTO;
import com.victormacedov.library_api.controller.mappers.UsuarioMapper;
import com.victormacedov.library_api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private void salvar(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        var usuario = usuarioMapper.toEntity(usuarioDTO);
        usuarioService.salvarUsuario(usuario);
    }
}
