package com.victormacedov.library_api.repository;

import com.victormacedov.library_api.service.TransacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransacoesTest {

    @Autowired
    private TransacaoService transacaoService;

    @Test
    public void executarTransacaoTest() {
        transacaoService.executarTransacao();
    }

    @Test
    public void atualizacaoSemAtualizarTest() {
        transacaoService.atualizacaoSemAtualizar();
    }
}
