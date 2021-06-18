package com.presto.restcontroller;

import com.presto.dto.UsuarioDTO;
import com.presto.dto.UsuarioFrontDTO;
import com.presto.model.Restaurante;
import com.presto.model.Usuario;
import com.presto.repository.RestauranteRepository;
import com.presto.repository.UsuarioRepository;
import com.presto.service.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UsuarioDTO usuarioDTO){
        ResponseEntity resp = this.usuarioService.validaEmail(usuarioDTO.getEmail());
        if (resp.getStatusCode() != HttpStatus.OK){return ResponseEntity.badRequest().build();}
        Usuario usuario = new Usuario(usuarioDTO);
        usuario.setSenha(this.passwordEncoder.encode(usuario.getPassword()));
        Optional<Restaurante> restaurante = this.restauranteRepository.findByNome(usuarioDTO.getNomeRestaurante());
        if (restaurante.isPresent()){
            usuario.getRestaurantes().add(restaurante.get());
            usuario = this.usuarioRepository.save(usuario);
            restaurante.get().getUsuarios().add(usuario);
            this.restauranteRepository.save(restaurante.get());
            return new ResponseEntity<>(new UsuarioDTO(usuario), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
        
    }

    @PostMapping("/createAdmin/{id}")
    public ResponseEntity<?> createUserAdimin(@RequestBody UsuarioDTO usuarioDTO, @PathVariable("id") Long id){
        if (this.usuarioService.isAdmin(id)) {
            ResponseEntity resp = this.usuarioService.validaEmail(usuarioDTO.getEmail());
            if (resp.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.badRequest().build();
            }
            Usuario usuario = new Usuario(usuarioDTO, "ADMIN");
            usuario.setSenha(this.passwordEncoder.encode(usuario.getPassword()));
            Optional<Restaurante> restaurante = this.restauranteRepository.findByNome(usuarioDTO.getNomeRestaurante());
            if (restaurante.isPresent()) {
                usuario.getRestaurantes().add(restaurante.get());
                usuario = this.usuarioRepository.save(usuario);
                restaurante.get().getUsuarios().add(usuario);
                this.restauranteRepository.save(restaurante.get());
                return new ResponseEntity<>(new UsuarioDTO(usuario), HttpStatus.OK);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuarioFrontDTO> getByEmail(@PathVariable("email") String email){
       Optional<Usuario> usuario = this.usuarioRepository.findByEmail(email);
       if (usuario.isPresent()){
           UsuarioFrontDTO dto =  new UsuarioFrontDTO(usuario.get());
           return  ResponseEntity.ok(dto);
       }
       return ResponseEntity.notFound().build();

    }

    @PutMapping("redefinirsenha/{email}")
    public ResponseEntity<?> redefinirSenha(@PathVariable("email") String email){
        return this.usuarioService.enviaSenhaPorEmail(email);

    }

}
