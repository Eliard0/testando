package com.presto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Restaurante implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    private String nome;

    @OneToOne(mappedBy = "cardapioRestaurante")
    private Cardapio cardapio;

    @OneToMany(mappedBy = "produtoRestaurante")
    private List<Produto> produto  = new ArrayList<>();

    @OneToMany(mappedBy = "mesaRestaurante")
    private List<Mesa> mesa  = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "usuarios_restaurantes",
            joinColumns = @JoinColumn(name = "restaurante_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    @JsonIgnore
    private List<Usuario> usuarios = new ArrayList<>();

    public Restaurante(){}

    public Restaurante(long id){
      this.id = id;
    }

    public Restaurante(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }


    @JsonIgnore
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public String getNome(){
      return this.nome;
    }

    public Long getId(){
      return this.id;
    }
    public void setNome(String nome){
      this.nome = nome;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cardapio getCardapio() {
        return  cardapio;
    }

    public void addCardapio(Cardapio cardapio) {
        this.cardapio = cardapio;
    }

    public List<Produto> getProduto() {
        return produto;
    }

    public void setProdutoList(List<Produto> produto) {
        this.produto = produto;
    }

    public void addProduto(Produto produto) {
        this.produto.add(produto);
    }

    public List<Mesa> getMesa() {
        return mesa;
    }

    public void setMesaList(List<Mesa> mesa) {
        this.mesa = mesa;
    }
    public void addMesa(Mesa mesa) {
        this.mesa.add(mesa);
    }
}