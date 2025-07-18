package com.victormacedov.library_api.repository;

import com.victormacedov.library_api.model.Autor;
import com.victormacedov.library_api.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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


}
