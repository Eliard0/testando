package com.presto.repository;


import com.presto.model.Restaurante;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface RestauranteRepository extends JpaRepositoryImplementation<Restaurante, Long> {
    Optional<Restaurante> findByNome(String nome);
}
