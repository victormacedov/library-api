package com.victormacedov.library_api.controller;

import com.victormacedov.library_api.controller.dto.AutorDTO;
import com.victormacedov.library_api.controller.dto.ErroResposta;
import com.victormacedov.library_api.exceptions.OperacaoNaoPermitidaException;
import com.victormacedov.library_api.exceptions.RegistroDuplicadoException;
import com.victormacedov.library_api.model.Autor;
import com.victormacedov.library_api.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService autorService;

    @PostMapping
    public ResponseEntity<Object> salvarAutor(@RequestBody @Valid AutorDTO autorDTO) {
        try{
            Autor autor = autorDTO.mapearParaAutor();
            autorService.salvarAutor(autor);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(autor.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (RegistroDuplicadoException e) {
            var erroResposta = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroResposta.status()).body(erroResposta);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> obterDetalhesPorId(@PathVariable String id){
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
        if (autorOptional.isPresent()) {
            Autor autor = autorOptional.get();
            AutorDTO autorDTO = new AutorDTO(autor.getId(), autor.getNome(),
                                              autor.getDataNascimento(), autor.getNacionalidade());
            return ResponseEntity.ok().body(autorDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarAutorPorId(@PathVariable String id) {
        try{
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.obterPorId(idAutor);
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            autorService.deletarAutor(autorOptional.get());
            return ResponseEntity.noContent().build();
        } catch (OperacaoNaoPermitidaException e) {
            var erroResposta = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroResposta.status()).body(erroResposta);
        }
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> filtrarAutores(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {

        List<Autor> listaDaConsulta = autorService.pesquisaByExample(nome, nacionalidade);
        List<AutorDTO> listaDosAutores = listaDaConsulta
                .stream()
                .map(autor -> new AutorDTO(
                        autor.getId(),
                        autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(listaDosAutores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarAutor(@PathVariable String id, @RequestBody @Valid AutorDTO autorDTO) {

        try{
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
        } catch (RegistroDuplicadoException e) {
            var erroResposta = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroResposta.status()).body(erroResposta);
        }
    }
}
