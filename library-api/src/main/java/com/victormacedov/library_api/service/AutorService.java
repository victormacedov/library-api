package com.victormacedov.library_api.service;

import com.victormacedov.library_api.exceptions.OperacaoNaoPermitidaException;
import com.victormacedov.library_api.model.Autor;
import com.victormacedov.library_api.model.Usuario;
import com.victormacedov.library_api.repository.AutorRepository;
import com.victormacedov.library_api.repository.LivroRepository;
import com.victormacedov.library_api.security.SecurityService;
import com.victormacedov.library_api.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;
    private final AutorValidator autorValidator;
    private final LivroRepository livroRepository;
    private final SecurityService securityService;

    public void salvarAutor(Autor autor) {
        autorValidator.validar(autor);
        Usuario usuario = securityService.obterUsuarioLogado();
        autor.setIdUsuarioUltimaAtualizacao(usuario);
        autorRepository.save(autor);
    }

    public void atualizarAutor(Autor autor) {
        if (autor.getId() == null) {
            throw new IllegalArgumentException("Para atualizar, é necessário que o autor esteja salvo na base.");
        }
        autorValidator.validar(autor);
        autorRepository.save(autor);
    }

    public Optional<Autor> obterPorId(UUID idAutor) {
        return autorRepository.findById(idAutor);
    }

    public void deletarAutor(Autor autor) {
        if (possuiLivro(autor)) {
            throw new OperacaoNaoPermitidaException("Não é possível excluir o autor, pois ele possui livros cadastrados.");
        }

        autorRepository.delete(autor);
    }

    public List<Autor> pesquisaByExample(String nome, String nacionalidade) {
        var autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher exampleMatcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "dataNascimento", "livro", "dataCadastro", "dataUltimaAtualizacao", "idUsuarioUltimaAtualizacao")
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Autor> autorExample = Example.of(autor, exampleMatcher);

        return autorRepository.findAll(autorExample);
    }

    public boolean possuiLivro(Autor autor){
        return livroRepository.existsByAutor(autor);
    }
}
