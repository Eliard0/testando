package com.presto.dto;

import com.presto.model.Usuario;

public class UsuarioDTO {
    private long id;

    private String nome;

    private String email;

    private String senha;

    private String dataNascimento;

    private String nomeRestaurante;

    public UsuarioDTO() {
    }

    public UsuarioDTO(long id, String nome, String email, String senha, String dataNascimento, String nomeRestaurante) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.nomeRestaurante = nomeRestaurante;
    }

    public UsuarioDTO(Usuario usuario) {
        if (usuario != null){
            this.id = usuario.getId();
            this.nome = usuario.getNome();
            this.email = usuario.getEmail();
            this.dataNascimento = usuario.getDataNascimento();
            this.nomeRestaurante = usuario.getRestaurantes().isEmpty() ? "" : usuario.getRestaurantes().get(0).getNome();
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    public void setNomeRestaurante(String nomeRestaurante) {
        this.nomeRestaurante = nomeRestaurante;
    }
}
