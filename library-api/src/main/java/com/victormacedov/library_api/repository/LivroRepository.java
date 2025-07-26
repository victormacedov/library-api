package com.victormacedov.library_api.repository;

import com.victormacedov.library_api.model.Autor;
import com.victormacedov.library_api.model.Livro;
import com.victormacedov.library_api.model.enums.GeneroLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {

    List<Livro> findByAutor(Autor autor);

    List<Livro> findByTitulo(String titulo);

    List<Livro> findByIsbn(String isbn);

    List<Livro> findByTituloAndPreco(String titulo, BigDecimal preco);

    List<Livro> findByTituloOrIsbn(String titulo, String isbn);

    List<Livro> findByDataPublicacaoBetweenOrderByDataPublicacao(LocalDate inicio, LocalDate fim);

    @Query("select l from Livro as l order by l.titulo, l.preco")
    List<Livro> listarTodosOsLivrosOrdenadoPorTituloEPreco();

    @Query("select a from Livro l join l.autor a")
    List<Autor> listarAutoresDosLivros();

    @Query("select distinct l.titulo from Livro l")
    List<String> listarNomesNaoRepetidosLivros();

    @Query("""
    select l.genero, a.nome, l.titulo from Livro l
    join l.autor a
    where a.nacionalidade = 'Brasileiro'
    order by l.genero
    """)
    List<String> listarGenerosAutoresBrasileiros();

    @Query("select l from Livro l where l.genero = :genero order by :paramOrdenacao")
    List<Livro> findByGeneroNamedParam(
            @Param("genero") GeneroLivro generoLivro,
            @Param("paramOrdenacao") String nomePropriedadeOrdenacao);

    @Query("select l from Livro l where l.genero = ?1 order by ?2")
    List<Livro> findByGeneroPositionalParam(
            @Param("genero") GeneroLivro generoLivro,
            @Param("paramOrdenacao") String nomePropriedadeOrdenacao);

    @Modifying
    @Transactional
    @Query("delete from Livro where genero = ?1")
    void deleteByGenero(GeneroLivro generoLivro);

    @Modifying
    @Transactional
    @Query("update Livro set dataPublicacao = ?1 where id = ?2")
    void updateDataPublicacao(LocalDate novaData, UUID idLivro);

    boolean existsByAutor(Autor autor);
}
