package com.presto.service;

import com.presto.dto.RestauranteDTO;
import com.presto.model.Restaurante;
import com.presto.repository.RestauranteRepository;
import com.presto.util.JavaMailApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class RestauranteServiceImp implements RestauranteService {
    @Autowired
    RestauranteRepository restauranteRepository;

    @Override
    public ResponseEntity<?> validaEmail(String email) {
//        Optional<Restaurante> usuario = restauranteRepository.findByEmailContaining(email);
//        if(usuario.isPresent()){
//            return new ResponseEntity<>("Usuario existente!", HttpStatus.BAD_REQUEST);
//        }
        return new ResponseEntity<>("Usuario não existe. Pode cadastrar!", HttpStatus.OK);
    }



    @Override
    public Restaurante transformarParaUsuario(RestauranteDTO restauranteDTO) {
        return new Restaurante(restauranteDTO.getId(),restauranteDTO.getNome());
    }


}
