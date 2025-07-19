package com.victormacedov.library_api.service;

import com.victormacedov.library_api.model.Autor;
import com.victormacedov.library_api.model.Livro;
import com.victormacedov.library_api.model.enums.GeneroLivro;
import com.victormacedov.library_api.repository.AutorRepository;
import com.victormacedov.library_api.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public void atualizacaoSemAtualizar(){
        var livro = livroRepository.findById(UUID.fromString("42574fb9-9a21-4671-9d34-73e8d4844e09")).orElse(null);

        livro.setDataPublicacao(LocalDate.of(2012, 12, 12));
    }

    @Transactional
    public void executarTransacao() {
        Autor autor = new Autor();
        autor.setNome("Autor de Teste Transacao");
        autor.setNacionalidade("Brasileiro");
        autor.setDataNascimento(LocalDate.of(1980, 1, 1));

        autorRepository.save(autor);
        //autorRepository.saveAndFlush(autor);

        Livro livro = new Livro();
        livro.setAutor(autor);
        livro.setIsbn("1234567891098");
        livro.setTitulo("Livro de Teste Transacao");
        livro.setDataPublicacao(LocalDate.of(2020, 12, 1));
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setPreco(BigDecimal.valueOf(29.99));

        livroRepository.save(livro);

        if (autor.getNome().equals("Teste")) {
            throw new RuntimeException("Erro simulado para testar transação - rollback");
        }
        System.out.println("Transação executada com sucesso: Autor e Livro salvos - commit");
    }


}
