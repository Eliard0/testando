package com.presto.restcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.presto.dto.RestauranteDTO;
import com.presto.model.*;
import com.presto.repository.*;

import com.presto.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurante")
public class RestauranteController {

  @Autowired
  RestauranteRepository restauranteRepository;
  @Autowired
  MesaRepository mesaRepository;
  @Autowired
  CardapioRepository cardapioRepository;
  @Autowired
  ProdutoRepository produtoRepository;
  @Autowired
  UsuarioRepository usuarioRepository;

  @Autowired
  RestauranteService restauranteService;

  @PreAuthorize(value = "hasAnyRole('ADMIN')")
  @GetMapping("/restaurantes")
  public ResponseEntity<List<Restaurante>> getAllRestaurantes() {
    try {
      List<Restaurante> restaurantes = restauranteRepository.findAll();
      if(restaurantes.isEmpty()){ return ResponseEntity.noContent().build(); }
      return new ResponseEntity<>(restaurantes, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
    @GetMapping("/restaurantes/nomes")
    public ResponseEntity<List<String>> getAllRestaurantesByName() {
        try {
            List<Restaurante> restaurantes = restauranteRepository.findAll();
            if(restaurantes.isEmpty()){ return ResponseEntity.noContent().build(); }
            List<String> nomes = new ArrayList<>();
            restaurantes.forEach(r -> nomes.add(r.getNome()));
            return new ResponseEntity<>(nomes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

  @GetMapping("/getById/{id}")
  public ResponseEntity<Restaurante> getUsuarioById(@PathVariable("id") long id) {
    Optional<Restaurante> usuarioData = restauranteRepository.findById(id);
    return ResponseEntity.of(usuarioData);
  }

@PreAuthorize(value = "hasAnyRole('ADMIN')")
  @PostMapping("/create/{idUsuario}")
  public ResponseEntity<?> createRestaurante(@RequestBody RestauranteDTO restauranteDTO, @PathVariable("idUsuario") long id) {
    try {
        Optional<Usuario> _usuario = usuarioRepository.findById(id);
        if (_usuario.isPresent()){
            Usuario usuario = _usuario.get();
            Restaurante restaurante = restauranteService.transformarParaUsuario(restauranteDTO);
            restaurante.getUsuarios().add(usuario);
            restaurante = restauranteRepository.save(restaurante);
            usuario.getRestaurantes().add(restaurante);
            this.usuarioRepository.save(usuario);
            return new ResponseEntity<>(restaurante, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();

    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
    }
  }

@PreAuthorize(value = "hasAnyRole('ADMIN')")
@PutMapping("/update/{id}")
  public ResponseEntity<Restaurante> updateUsuario(@PathVariable("id") long id, @RequestBody Restaurante restaurante) {
    Optional<Restaurante> restauranteData = restauranteRepository.findById(id);

    if (restauranteData.isPresent()) {
      Restaurante _restaurante = restauranteData.get();

      _restaurante.setNome(restaurante.getNome());

      return new ResponseEntity<>(restauranteRepository.save(_restaurante), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

 @PreAuthorize(value = "hasAnyRole('ADMIN')")
 @DeleteMapping("/delete/{id}")
  public ResponseEntity<HttpStatus> deleteUsuario(@PathVariable("id") long id) {
    try {
      restauranteRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }
  }

  @PreAuthorize(value = "hasAnyRole('ADMIN')")
  @DeleteMapping("/delete")
  public ResponseEntity<HttpStatus> deleteAllUsuarios() {
    try {
      restauranteRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
    }

  }



  //---------------------PUT DE TABELAS ASSOCIADAS ------------------------------------//








  @PreAuthorize(value = "hasAnyRole('ADMIN')")
  @PutMapping("/mesaadd/{id}")
  public ResponseEntity<?> mesaAdd(@PathVariable("id") long id, @RequestBody Mesa mesa){
    Optional<Restaurante> restaurante = restauranteRepository.findById(id);

    if (restaurante.isPresent()){
      mesa.setRestaurante(restaurante.get());
      Mesa mesa1 = mesaRepository.save(mesa);

        restaurante.get().addMesa(mesa1);
      restauranteRepository.save(restaurante.get());
      return new ResponseEntity<>(mesa1, HttpStatus.OK);
    }
    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
  }
  
  @PreAuthorize(value = "hasAnyRole('ADMIN')")
  @PutMapping("/cardapioadd/{id}")
  public ResponseEntity<?> cardapioAdd(@PathVariable("id") long id, @RequestBody Cardapio cardapio){
    Optional<Restaurante> usuario = restauranteRepository.findById(id);

    if (usuario.isPresent()){
      cardapio.setUsuario(usuario.get());
      Cardapio cardapio1 = cardapioRepository.save(cardapio);

      usuario.get().addCardapio(cardapio1);
      restauranteRepository.save(usuario.get());
      return new ResponseEntity<>(cardapio1, HttpStatus.OK);
    }
    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
  }
  @PreAuthorize(value = "hasAnyRole('ADMIN')")
  @PutMapping("/produtoadd/{id}")
  public ResponseEntity<?> produtoAdd(@PathVariable("id") long id, @RequestBody Produto produto){
    Optional<Restaurante> usuario = restauranteRepository.findById(id);

    if (usuario.isPresent()){
      produto.setUsuarioProduto(usuario.get());
      Produto produto1 = produtoRepository.save(produto);

      usuario.get().addProduto(produto1);
      restauranteRepository.save(usuario.get());
      return new ResponseEntity<>(produto1, HttpStatus.OK);
    }
    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
  }
}