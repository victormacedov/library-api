package com.victormacedov.library_api.controller.dto;

import java.util.List;

public record UsuarioDTO(String login, String senha, List<String> roles) {
}
