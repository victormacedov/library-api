package com.victormacedov.library_api.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UsuarioDTO(
        @NotBlank(message = "Campo Obrigatório.")
        String login,
        @NotBlank(message = "Campo Obrigatório.")
        String senha,
        @Email(message = "Email inválido.")
        String email,
        List<String> roles) {
}
