package com.victormacedov.library_api.service;

import com.victormacedov.library_api.model.Livro;
import com.victormacedov.library_api.model.Usuario;
import com.victormacedov.library_api.model.enums.GeneroLivro;
import com.victormacedov.library_api.repository.LivroRepository;
import com.victormacedov.library_api.repository.specs.LivroSpecs;
import com.victormacedov.library_api.security.SecurityService;
import com.victormacedov.library_api.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroValidator livroValidator;
    private final SecurityService securityService;

    public Livro salvarLivro(Livro livro) {
        livroValidator.validarIsbnUnico(livro);
        Usuario usuario = securityService.obterUsuarioLogado();
        livro.setIdUsuarioUltimaAtualizacao(usuario);
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterLivroPorId(UUID id){
        return livroRepository.findById(id);
    }

    public void deletarLivro(Livro livro){
        livroRepository.delete(livro);
    }

    public Page<Livro> pesquisaComFiltros(
            String isbn,
            String titulo,
            String nomeAutor,
            GeneroLivro genero,
            Integer AnoPublicacao,
            Integer pagina,
            Integer tamanhoPagina) {
        Specification<Livro> specs = (root, query, cb) -> cb.conjunction();

        if(isbn != null){
            specs = specs.and(LivroSpecs.isbnEqual(isbn));
        }

        if (titulo != null){
            specs = specs.and(LivroSpecs.tituloLike(titulo));
        }

        if (nomeAutor != null){
            specs = specs.and(LivroSpecs.nomeAutorLike(nomeAutor));
        }

        if (genero != null){
            specs = specs.and(LivroSpecs.generoEqual(genero));
        }

        if (AnoPublicacao != null){
            specs = specs.and(LivroSpecs.anoPublicacaoEqual(AnoPublicacao));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return livroRepository.findAll(specs, pageRequest);
    }

    public void atualizarLivro(Livro livro) {
        if (livro.getId() == null) {
            throw new IllegalArgumentException("Para atualizar, é necessário que o livro esteja salvo na base.");
        }

        livroValidator.validarIsbnUnico(livro);
        livroRepository.save(livro);
    }
}
