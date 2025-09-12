package com.victormacedov.library_api.controller;

import com.victormacedov.library_api.controller.dto.AutorDTO;
import com.victormacedov.library_api.controller.mappers.AutorMapper;
import com.victormacedov.library_api.model.Autor;
import com.victormacedov.library_api.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Endpoints para gerenciamento de autores")
@Slf4j
public class AutorController implements GenericController {

    private final AutorService autorService;
    private final AutorMapper autorMapper;

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Salvar Autor", description = "Salva um novo autor.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Autor salvo com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados do autor."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
    public ResponseEntity<Void> salvarAutor(@RequestBody @Valid AutorDTO autorDTO) {
        log.info("Cadastrando novo Autor: {}", autorDTO.nome());

        Autor autor = autorMapper.toAutor(autorDTO);
        autorService.salvarAutor(autor);

        URI location = gerarHeaderLocation(autor.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Obter Detalhes do Autor", description = "Retorna os detalhes de um autor pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado.")
    })
    public ResponseEntity<AutorDTO> obterDetalhesPorId(@PathVariable String id) {
        var idAutor = UUID.fromString(id);

        return autorService
                .obterPorId(idAutor)
                .map(autor -> {
                    AutorDTO autorDTO = autorMapper.toAutorDTO(autor);
                    return ResponseEntity.ok(autorDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Deletar Autor", description = "Deleta um autor pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "400", description = "Autor possui livros associados e não pode ser deletado.")
    })
    public ResponseEntity<Void> deletarAutorPorId(@PathVariable String id) {
        log.info("Deletando Autor de ID: {}", id);

        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        autorService.deletarAutor(autorOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    @Operation(summary = "Pesquisar Autor", description = "Pesquisa autores por nome ou nacionalidade.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Encontrados.")
    })
    public ResponseEntity<List<AutorDTO>> filtrarAutores(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {

        List<Autor> listaDaConsulta = autorService.pesquisaByExample(nome, nacionalidade);
        List<AutorDTO> listaDosAutores = listaDaConsulta
                .stream()
                .map(autorMapper::toAutorDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(listaDosAutores);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Atualizar Autor", description = "Atualiza um autor existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
    public ResponseEntity<Void> atualizarAutor(@PathVariable String id, @RequestBody @Valid AutorDTO autorDTO) {

        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        log.info("Atualizando Autor: {}", id);

        var autor = autorOptional.get();
        autor.setNome(autorDTO.nome());
        autor.setDataNascimento(autorDTO.dataNascimento());
        autor.setNacionalidade(autorDTO.nacionalidade());
        autorService.atualizarAutor(autor);

        return ResponseEntity.noContent().build();
    }
}
