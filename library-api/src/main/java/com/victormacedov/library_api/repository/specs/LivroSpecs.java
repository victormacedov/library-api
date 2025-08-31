package com.victormacedov.library_api.repository.specs;

import com.victormacedov.library_api.model.Livro;
import com.victormacedov.library_api.model.enums.GeneroLivro;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpecs {

    public static Specification<Livro> isbnEqual(String isbn) {
        return (root, query, cb) ->
                cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Livro> tituloLike(String titulo) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");
    }

    public static Specification<Livro> nomeAutorLike(String nomeAutor) {
        return (root, query, cb) -> {
            return cb.like(cb.upper(root.get("autor").get("nome")), "%" + nomeAutor.toUpperCase() + "%");
        };
    }

    public static Specification<Livro> generoEqual(GeneroLivro genero){
        return (root, query, cb) ->
                cb.equal(root.get("genero"), genero);
    }

    public static Specification<Livro> anoPublicacaoEqual(Integer anoPublicacao){
        return (root, query, cb) ->
                cb.equal(cb.function("to_char", String.class, root.get("dataPublicacao"), cb.literal("YYYY")), anoPublicacao.toString());
    }
}
