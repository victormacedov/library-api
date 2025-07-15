package com.victormacedov.library_api.repository;

import com.victormacedov.library_api.model.Autor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository autorRepository;

    @Test
    public void salvarAutorTest(){
        Autor autor = new Autor();
        autor.setNome("Autor Teste 01");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1998, 8, 8));

        var autorSalvo = autorRepository.save(autor);
        System.out.println("Autor salvo: " + autorSalvo.getId() + " - " + autorSalvo.getNome());
    }

    @Test
    public void atualizarAutorTest(){
        var id = UUID.fromString("6d7889ad-547a-448f-8ee9-8c8bb28700d6");

        Optional<Autor> autorOptional = autorRepository.findById(id);
        if (autorOptional.isPresent()) {
            Autor autorEncontrado = autorOptional.get();
            System.out.println("Dados do autor: \n" + autorEncontrado);

            autorEncontrado.setDataNascimento(LocalDate.of(2000, 01, 01));

            autorRepository.save(autorEncontrado);
        }
    }

    @Test
    public void listarAutoresTest() {
        List<Autor> lista = autorRepository.findAll();
        System.out.println("Lista de autores: ");
        lista.forEach(autor -> System.out.println(autor.getId() + " - " + autor.getNome()));
    }

    @Test
    public void contarAutoresTest() {
        System.out.println("Total de autores: " + autorRepository.count());
    }

    @Test
    public void deletarAutorPorIdTest() {
        var id = UUID.fromString("ed6921b6-7656-4e48-aff2-cb7600862527");
        autorRepository.deleteById(id);
        System.out.println("Autor com ID " + id + " deletado.");
    }

    @Test
    public void deletarPorObjetoTest(){
        var id = UUID.fromString("e81964b2-021e-4671-89ee-5dad29d3c7be");
        var autor = autorRepository.findById(id).get();
        autorRepository.delete(autor);
        System.out.println("Autor deletado: " + autor.getId() + " - " + autor.getNome());
    }
}
