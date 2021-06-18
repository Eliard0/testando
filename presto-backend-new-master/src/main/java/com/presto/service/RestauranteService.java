package com.presto.service;


import com.presto.dto.RestauranteDTO;
import com.presto.model.Restaurante;
import org.springframework.http.ResponseEntity;

public interface RestauranteService {

    ResponseEntity<?> validaEmail(String email);
    Restaurante transformarParaUsuario(RestauranteDTO restauranteDTO);
}
