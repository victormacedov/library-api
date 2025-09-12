package com.victormacedov.library_api.controller;

import com.victormacedov.library_api.controller.dto.UsuarioDTO;
import com.victormacedov.library_api.controller.mappers.UsuarioMapper;
import com.victormacedov.library_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para cadastro de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salvar Usuário", description = "Salva um novo usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário salvo com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados do usuário."),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    private void salvar(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        var usuario = usuarioMapper.toEntity(usuarioDTO);
        usuarioService.salvarUsuario(usuario);
    }
}
