package com.victormacedov.library_api.controller;

import com.victormacedov.library_api.controller.dto.CadastroLivroDTO;
import com.victormacedov.library_api.controller.dto.ResultadoPesquisaLivroDTO;
import com.victormacedov.library_api.controller.mappers.LivroMapper;
import com.victormacedov.library_api.model.Livro;
import com.victormacedov.library_api.model.enums.GeneroLivro;
import com.victormacedov.library_api.service.AutorService;
import com.victormacedov.library_api.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController implements GenericController{

    private final LivroService livroService;
    private final LivroMapper livroMapper;
    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<Object> salvarLivro(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO){
        Livro livro = livroMapper.toLivro(cadastroLivroDTO);
        livroService.salvarLivro(livro);
        var url = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(url).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable String id){
        return livroService.obterLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    var dto = livroMapper.toDTO(livro);
                    return  ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarLivro(@PathVariable String id) {
        return livroService.obterLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    livroService.deletarLivro(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping()
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

        Page<Livro> paginaResultado = livroService.pesquisaComFiltros(isbn, titulo, nomeAutor, genero, AnoPublicacao, pagina, tamanhoPagina);

        Page<ResultadoPesquisaLivroDTO> resultado = paginaResultado.map(livroMapper::toDTO);

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarLivro(@PathVariable String id, @RequestBody @Valid CadastroLivroDTO cadastroLivroDTO){
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
