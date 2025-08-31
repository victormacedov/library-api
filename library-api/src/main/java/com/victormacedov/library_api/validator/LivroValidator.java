package com.victormacedov.library_api.validator;

import com.victormacedov.library_api.exceptions.CampoInvalidoException;
import com.victormacedov.library_api.exceptions.RegistroDuplicadoException;
import com.victormacedov.library_api.model.Livro;
import com.victormacedov.library_api.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {

    private static final int ANO_LIMITE_PRECO_OBRIGATORIO = 2020;

    private final LivroRepository livroRepository;

    public void validarIsbnUnico(Livro livro) {
        if (existsByIsbn(livro)) {
            throw new RegistroDuplicadoException("ISBN já cadastrado no sistema.");
        }

        if (isPrecoObrigatorioNulo(livro)) {
            throw new CampoInvalidoException("preco", "Para livros com o ano de publicação a partir de 2020, informar o preço é obrigatório.");
        }
    }

    private boolean isPrecoObrigatorioNulo(Livro livro) {
        return livro.getPreco() == null && livro.getDataPublicacao().getYear() >= ANO_LIMITE_PRECO_OBRIGATORIO;
    }

    private boolean existsByIsbn(Livro livro) {
        Optional<Livro> livroEncontrado = livroRepository.findByIsbn(livro.getIsbn());

        if (livro.getId() == null) {
            return livroEncontrado.isPresent();
        }
        return livroEncontrado
                .map(Livro::getId)
                .stream()
                .anyMatch(id -> !id.equals(livro.getId()));
    }
}
