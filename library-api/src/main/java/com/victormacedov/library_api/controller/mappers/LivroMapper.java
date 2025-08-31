package com.victormacedov.library_api.controller.mappers;

import com.victormacedov.library_api.controller.dto.CadastroLivroDTO;
import com.victormacedov.library_api.controller.dto.ResultadoPesquisaLivroDTO;
import com.victormacedov.library_api.model.Livro;
import com.victormacedov.library_api.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;

    @Mapping(target = "autor", expression = "java(autorRepository.findById(cadastroLivroDTO.idAutor()).orElse(null))")
    public abstract Livro toLivro(CadastroLivroDTO cadastroLivroDTO);

    public abstract ResultadoPesquisaLivroDTO toDTO(Livro livro);
}
