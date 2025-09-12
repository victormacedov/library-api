package com.victormacedov.library_api.controller;

import com.victormacedov.library_api.controller.dto.CadastroLivroDTO;
import com.victormacedov.library_api.controller.dto.ResultadoPesquisaLivroDTO;
import com.victormacedov.library_api.controller.mappers.LivroMapper;
import com.victormacedov.library_api.model.Livro;
import com.victormacedov.library_api.model.enums.GeneroLivro;
import com.victormacedov.library_api.service.AutorService;
import com.victormacedov.library_api.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livro", description = "Endpoints para gerenciamento de livros")
@Slf4j
public class LivroController implements GenericController{

    private final LivroService livroService;
    private final LivroMapper livroMapper;
    private final AutorService autorService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Salvar Livro", description = "Salva um novo livro.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro salvo com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados do livro."),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    public ResponseEntity<Object> salvarLivro(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO){
        log.info("Recebida requisição para salvar livro: {}", cadastroLivroDTO);

        Livro livro = livroMapper.toLivro(cadastroLivroDTO);
        livroService.salvarLivro(livro);
        var url = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter Detalhes do Livro", description = "Retorna os detalhes de um livro pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable String id){
        log.info("Buscando detalhes do livro com id: {}", id);

        return livroService.obterLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    var dto = livroMapper.toDTO(livro);
                    return  ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Deletar Livro", description = "Deleta um livro pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
            @ApiResponse(responseCode = "400", description = "Livro não pode ser deletado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    public ResponseEntity<Object> deletarLivro(@PathVariable String id) {
        log.info("Recebida requisição para deletar livro com id: {}", id);

        return livroService.obterLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    livroService.deletarLivro(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisar Livro", description = "Pesquisa livros por título, autor ou gênero.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Encontrados."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisaLivros(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false)
            Integer AnoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10")
            Integer tamanhoPagina){
        log.info("Pesquisando livros com filtros - isbn: {}, titulo: {}, nomeAutor: {}, genero: {}, anoPublicacao: {}, pagina: {}, tamanhoPagina: {}",
                isbn, titulo, nomeAutor, genero, AnoPublicacao, pagina, tamanhoPagina);

        Page<Livro> paginaResultado = livroService.pesquisaComFiltros(isbn, titulo, nomeAutor, genero, AnoPublicacao, pagina, tamanhoPagina);

        Page<ResultadoPesquisaLivroDTO> resultado = paginaResultado.map(livroMapper::toDTO);

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Atualizar Livro", description = "Atualiza um livro existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado.")
    })
    public ResponseEntity<Object> atualizarLivro(@PathVariable String id, @RequestBody @Valid CadastroLivroDTO cadastroLivroDTO){
        log.info("Recebida requisição para atualizar livro com id: {}", id);

        return livroService.obterLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    Livro livroEntidade = livroMapper.toLivro(cadastroLivroDTO);
                    livroEntidade.setIsbn(livro.getIsbn());
                    livroEntidade.setTitulo(livro.getTitulo());
                    livroEntidade.setDataPublicacao(livro.getDataPublicacao());
                    livroEntidade.setGenero(livro.getGenero());
                    livroEntidade.setPreco(livro.getPreco());
                    livroEntidade.setAutor(livro.getAutor());

                    livroService.atualizarLivro(livroEntidade);

                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
