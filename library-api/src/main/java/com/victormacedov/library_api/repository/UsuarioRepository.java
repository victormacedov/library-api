package com.victormacedov.library_api.repository;

import com.victormacedov.library_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Usuario findByLogin(String login);

    Usuario findByEmail(String email);
}
