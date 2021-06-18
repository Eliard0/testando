package com.presto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cardapio implements Serializable {

	private static final long serialVersionUID = 1L;


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    private String nome;



    @OneToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante cardapioRestaurante;

    @JsonIgnore
    @OneToMany
    @JoinTable(name = "cardapio_produto",
            joinColumns = @JoinColumn(name = "cardapio_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private List<Produto> produtos = new ArrayList<>();



    public Cardapio(){}

    public Cardapio(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public long getId() {
        return id;
    }

    @JsonIgnore
    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setProdutos(Produto produto) {
        this.produtos.add(produto);
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    public Restaurante getUsuario() {
        return this.cardapioRestaurante;
    }

    public void setUsuario(Restaurante restaurante) {
        this.cardapioRestaurante = restaurante;
    }
}
