package com.victormacedov.library_api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
        UUID id,
        @NotBlank(message = "Campo Obrigatório.")
        @Size(min = 2, max = 100, message = "Campo fora do tamanho permitido. Deve ter entre 2 e 100 caracteres.")
        String nome,
        @NotNull(message = "Campo Obrigatório.")
        @Past(message = "Deve ser uma data passada.")
        LocalDate dataNascimento,
        @NotBlank(message = "Campo Obrigatório.")
        @Size(max = 50, message = "Campo fora do tamanho permitido. Deve ter no máximo 50 caracteres.")
        String nacionalidade) {
}
