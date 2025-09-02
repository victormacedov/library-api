package com.victormacedov.library_api.controller.mappers;

import com.victormacedov.library_api.controller.dto.UsuarioDTO;
import com.victormacedov.library_api.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO usuarioDTO);
}
