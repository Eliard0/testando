package com.presto.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import java.io.Serializable;

import java.util.List;

@Entity
public class Produto implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Cardapio cardapios;

    @JsonIgnore
    @ManyToMany(mappedBy = "itensDoPedido")
    private List<Pedido> pedido;
    private String nome;
    private String tipo;
    private String descricao;

    @JsonFormat(pattern = "HH:mm:ss")
    private Integer tempo;
    private String imagem;
    
    private double valor;

    private Integer quantidadeEstoque;

    private Integer contador= 0;

    private long cardapio_id;

    @JsonIgnore
    @ManyToOne
    @JoinTable(name = "restaurante_produtos",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurante_id"))
    private Restaurante produtoRestaurante;

    public Produto(){}

    public Produto(String nome, String tipo, Integer tempo, String descricao, String imagem, Integer quantidadeEstoque){
        this.nome = nome;
        this.tipo = tipo;
        this.tempo = tempo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.quantidadeEstoque = quantidadeEstoque;
        this.cardapio_id = this.cardapios.getId();
    }

    public List<Pedido> getPedido() {
        return this.pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido.add(pedido);
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getTempo() {
        return tempo;
    }

    public Cardapio getCardapios() {
        return cardapios;
    }

    public double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getImagem() {
        return imagem;
    }



    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }



    public void setCardapios(Cardapio cardapio) {
        if(cardapio==null) {
            this.cardapio_id = 0;
            this.cardapios = cardapio;
        }else if (cardapio!= null){
            this.cardapio_id = cardapio.getId();
            this.cardapios = cardapio;
        }

    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPedido(List<Pedido> pedido) {
        this.pedido = pedido;
    }

    @JsonIgnore
    public Restaurante getUsuarioProduto() {
        return this.produtoRestaurante;
    }

    public void setUsuarioProduto(Restaurante restauranteProduto) {
        this.produtoRestaurante = restauranteProduto;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Integer getContador() {
        return contador;
    }

    public void setContador(Integer contador) {
        this.contador = contador;
    }


    public long getRetornoDoCardapio_id() {
        return cardapio_id;
    }

    public void setRetornoDoCardapio_id(long cardapio_id) {
        this.cardapio_id = cardapio_id;
    }
}
