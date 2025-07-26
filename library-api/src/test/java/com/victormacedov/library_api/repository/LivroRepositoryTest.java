package com.victormacedov.library_api.repository;

import com.victormacedov.library_api.model.Autor;
import com.victormacedov.library_api.model.Livro;
import com.victormacedov.library_api.model.enums.GeneroLivro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class LivroRepositoryTest {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    public void salvarTest() {
        Livro livro = new Livro();
        livro.setIsbn("9788531904354");
        livro.setTitulo("Iracema");
        livro.setDataPublicacao(LocalDate.of(1865, 4, 26));
        livro.setGenero(GeneroLivro.ROMANCE);
        livro.setPreco(BigDecimal.valueOf(45.00));

        Autor autor = autorRepository.findById(UUID.fromString("b1ec9143-8cc2-425b-84e0-40538957968d")).orElse(null);
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

    @Test
    @Transactional
    public void buscarLivroEAutorTest(){
        UUID idLivro = UUID.fromString("29856710-15d6-4559-a39f-78cf506e7fde");
        Livro livro = livroRepository.findById(idLivro).orElse(null);
        System.out.println("Livro: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor().getNome());
    }

    @Test
    public void pesquisaPorTituloTest() {
        List<Livro> listaDeLivros = livroRepository.findByTitulo("A casa assombrada");
        listaDeLivros.forEach(System.out::println);
    }

    @Test
    public void pesquisaPorIsbnTest() {
        List<Livro> listaDeLivros = livroRepository.findByIsbn("2734985743985");
        listaDeLivros.forEach(System.out::println);
    }

    @Test
    public void pesquisaPorTituloEPrecoTest() {
        List<Livro> listaDeLivros = livroRepository.findByTituloAndPreco("Quincas Borba", BigDecimal.valueOf(85.00));
        listaDeLivros.forEach(System.out::println);
    }

    @Test
    public void pesquisaPorTituloOuIsbnTest() {
        List<Livro> listaDeLivros = livroRepository.findByTituloOrIsbn("O cientista maluco", "9788535910665");
        listaDeLivros.forEach(System.out::println);
    }

    @Test
    public void pesquisaPorDataPublicacaoEntreTest() {
        LocalDate inicio = LocalDate.of(2000, 1, 1);
        LocalDate fim = LocalDate.of(2020, 12, 31);
        List<Livro> listaDeLivros = livroRepository.findByDataPublicacaoBetweenOrderByDataPublicacao(inicio, fim);
        listaDeLivros.forEach(System.out::println);
    }

    @Test
    public void listarLivroComJPQLTest() {
        List<Livro> listaDeLivros = livroRepository.listarTodosOsLivrosOrdenadoPorTituloEPreco();
        listaDeLivros.forEach(System.out::println);
    }

    @Test
    @Transactional
    public void listarAutoresDosLivrosTest() {
        List<Autor> listaDeAutores = livroRepository.listarAutoresDosLivros();
        listaDeAutores.forEach(System.out::println);
    }

    @Test
    public void listarTitulosNaoRepetidosDosLivrosTest() {
        List<String> listaDeLivros = livroRepository.listarNomesNaoRepetidosLivros();
        listaDeLivros.forEach(System.out::println);
    }

    @Test
    public void listarGenerosAutoresBrasileirosTest() {
        List<String> listaDeLivros = livroRepository.listarGenerosAutoresBrasileiros();
        listaDeLivros.forEach(System.out::println);
    }

    @Test
    public void listarLivrosPorGeneroNamedParamTest() {
        var livro = livroRepository.findByGeneroNamedParam(GeneroLivro.MISTERIO, "preco");
        livro.forEach(System.out::println);
    }

    @Test
    public void listarLivrosPorGeneroPositionalParamTest() {
        var livro = livroRepository.findByGeneroPositionalParam(GeneroLivro.FICCAO, "dataPublicacao");
        livro.forEach(System.out::println);
    }

    @Test
    public void deletarLivrosPorGeneroTest() {
        livroRepository.deleteByGenero(GeneroLivro.BIOGRAFIA);
    }

    @Test
    public void atualizarDataPublicacaoDosLivrosTest() {
        LocalDate novaDataPublicacao = LocalDate.of(2023, 10, 1);
        UUID idLivro = UUID.fromString("8cb32136-9406-4198-8ddf-3ec01af377c0");
        livroRepository.updateDataPublicacao(novaDataPublicacao, idLivro);
    }
}
