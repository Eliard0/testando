package com.presto.repository;

import com.presto.model.Usuario;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepositoryImplementation<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
