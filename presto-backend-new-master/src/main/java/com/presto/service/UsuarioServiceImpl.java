package com.presto.service;


import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.presto.model.Usuario;
import com.presto.repository.UsuarioRepository;
import com.presto.util.JavaMailApp;
@Service
public class UsuarioServiceImpl implements UserDetailsService  {
    @Autowired
    UsuarioRepository usuarioRepository;

    public ResponseEntity<?> enviaSenhaPorEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if(usuario.isPresent()) {
            JavaMailApp javamail = new JavaMailApp();

            //--------------------Gerador de senha----------------------------------
            String senha;
            Random gerador = new Random();
            char[] letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 8; i++) {
                int ch = gerador.nextInt(letras.length);
                sb.append(letras[ch]);
            }
            senha = sb.toString();
            usuario.get().setSenha(senha);
            usuarioRepository.save(usuario.get());

            javamail.enviarEmail(email, senha);

            return new ResponseEntity<>("Email enviado!", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Usuario inexistente!", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public ResponseEntity<?> validaEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if(usuario.isPresent()){
            return new ResponseEntity<>("Usuario existente!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Usuario não existe. Pode cadastrar!", HttpStatus.OK);
    }

    public Boolean isAdmin(Long id){
        Optional<Usuario> usuario = this.usuarioRepository.findById(id);
        if(!usuario.isPresent()){return false;}
        Optional<Integer> isAdmin = usuario.get().getTipos().stream().filter(t -> t == 1).findFirst();
        if (isAdmin.isPresent()){
            return true;
        }
        return false;
    }

	@Override
	public Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
		if(usuario == null) {
			throw new UsernameNotFoundException(username + " não encontrado!");
		}
		return usuario.get();
	}

}
