package com.victormacedov.library_api.controller.mappers;

import com.victormacedov.library_api.controller.dto.AutorDTO;
import com.victormacedov.library_api.model.Autor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    @Mapping(source = "nome", target = "nome")
    Autor toAutor(AutorDTO autorDTO);

    AutorDTO toAutorDTO(Autor autor);
}
