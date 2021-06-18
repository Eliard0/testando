package com.presto.dto;

import com.presto.model.Restaurante;

import java.io.Serializable;

public class RestauranteDTO implements Serializable {
   
	private static final long serialVersionUID = 1L;

	private long id;

    private String nome;


    public RestauranteDTO() {
    }

    public RestauranteDTO(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    public RestauranteDTO(Restaurante res) {
        this.id = res.getId();
        this.nome = res.getNome();
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


}
