package com.victormacedov.library_api.controller.dto;

import com.victormacedov.library_api.model.enums.GeneroLivro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(
        @ISBN
        @NotBlank(message = "Campo Obrigatório.")
        String isbn,
        @NotBlank(message = "Campo Obrigatório.")
        String titulo,
        @NotNull(message = "Campo Obrigatório.")
        @Past(message = "Não é possível cadastrar livros com data de publicação futura.")
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        @NotNull(message = "Campo Obrigatório.")
        UUID idAutor
    ) {
}
