package com.victormacedov.library_api.controller;

import com.victormacedov.library_api.controller.dto.AutorDTO;
import com.victormacedov.library_api.controller.mappers.AutorMapper;
import com.victormacedov.library_api.model.Autor;
import com.victormacedov.library_api.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController implements GenericController {

    private final AutorService autorService;
    private final AutorMapper autorMapper;

    @PostMapping
    public ResponseEntity<Void> salvarAutor(@RequestBody @Valid AutorDTO autorDTO) {
        Autor autor = autorMapper.toAutor(autorDTO);
        autorService.salvarAutor(autor);

        URI location = gerarHeaderLocation(autor.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
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
    public ResponseEntity<Void> deletarAutorPorId(@PathVariable String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        autorService.deletarAutor(autorOptional.get());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
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
    public ResponseEntity<Void> atualizarAutor(@PathVariable String id, @RequestBody @Valid AutorDTO autorDTO) {

        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if (autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var autor = autorOptional.get();
        autor.setNome(autorDTO.nome());
        autor.setDataNascimento(autorDTO.dataNascimento());
        autor.setNacionalidade(autorDTO.nacionalidade());
        autorService.atualizarAutor(autor);

        return ResponseEntity.noContent().build();
    }
}
