package com.presto.dto;

import com.presto.model.Usuario;

import java.util.List;
import java.util.ArrayList;

public class UsuarioFrontDTO {
        private String nome;

        private String email;

        private List<RestauranteDTO> restaurantes = new ArrayList<>();

        public UsuarioFrontDTO(String nome, String email, List<RestauranteDTO> restaurantes) {
                this.nome = nome;
                this.email = email;

                this.restaurantes = restaurantes;
        }

        public UsuarioFrontDTO() {
        }

        public UsuarioFrontDTO(Usuario u) {
                this.nome = u.getNome();
                this.email = u.getEmail();
                if(!u.getRestaurantes().isEmpty()){
                   u.getRestaurantes().forEach(r -> this.restaurantes.add(new RestauranteDTO(r)));
                } 
          
        }

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public List<RestauranteDTO> getRestaurantes() {
                return restaurantes;
        }

        public void setRestaurantes(List<RestauranteDTO> restaurantes) {
                this.restaurantes = restaurantes;
        }
}
