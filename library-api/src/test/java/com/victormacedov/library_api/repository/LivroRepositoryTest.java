package com.victormacedov.library_api.repository;

import com.victormacedov.library_api.model.Autor;
import com.victormacedov.library_api.model.Livro;
import com.victormacedov.library_api.model.enums.GeneroLivro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
public class LivroRepositoryTest {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    public void salvarTest(){
        Livro livro = new Livro();
        livro.setIsbn("9780451524935");
        livro.setTitulo("1984");
        livro.setDataPublicacao(LocalDate.of(1949, 6, 8));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setPreco(BigDecimal.valueOf(70.00));

        Autor autor = autorRepository.findById(UUID.fromString("0e36bb24-d5ab-4c7c-9015-0f452ce2efe4")).orElse(null);
        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    public void salvarLivroEAutorTest(){
        Livro livro = new Livro();
        livro.setIsbn("9783127323207");
        livro.setTitulo("Outro Livro");
        livro.setDataPublicacao(LocalDate.of(1956, 5, 12));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setPreco(BigDecimal.valueOf(19.99));

        Autor autor = new Autor();
        autor.setNome("Outro Autor");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1965, 2, 13));

        autorRepository.save(autor);

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    public void salvarLivroEAutorCascadeTest(){
        Livro livro = new Livro();
        livro.setIsbn("9780451524935");
        livro.setTitulo("Livro Teste 02");
        livro.setDataPublicacao(LocalDate.of(1999, 9, 9));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setPreco(BigDecimal.valueOf(19.99));

        Autor autor = new Autor();
        autor.setNome("Autor Teste 02");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1999, 9, 9));

        livro.setAutor(autor);

        livroRepository.save(livro);
    }

    @Test
    public void atualizarAutorDoLivro(){

        UUID idLivro = UUID.fromString("e32d3116-1182-4fe8-9dc2-90ca8885e7fe");
        var livroParaAtualizar = livroRepository.findById(idLivro).orElse(null);

        UUID idAutor = UUID.fromString("af278726-96f2-4062-b2b3-11fc547456aa");
        Autor autor = autorRepository.findById(idAutor).orElse(null);

        livroParaAtualizar.setAutor(autor);
        livroRepository.save(livroParaAtualizar);
    }

    @Test
    public void deletarLivroPorIdTest() {
        UUID idLivro = UUID.fromString("23799737-cace-4003-9e77-397973fe554e");
        livroRepository.deleteById(idLivro);
    }

    @Test
    public void deletarLivroPorIdCascadeTest() {
        UUID idLivro = UUID.fromString("51517380-2ef6-4f8a-98ab-c75f42a0f1d0");
        livroRepository.deleteById(idLivro);
    }

}
