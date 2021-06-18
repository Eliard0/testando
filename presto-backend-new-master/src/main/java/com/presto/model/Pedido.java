package com.presto.model;



import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

import java.util.List;

@Entity
public class Pedido implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    private String nome;

    private String descricao;

    private double valorTotal;

    private long maiorTempo;

    private long tempoIdeal;

    private long horaQueFoiPedido;

    private String nomeCliente;



    @ManyToMany
    @JoinTable(name = "pedido_produto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private List<Produto> itensDoPedido;

    public Pedido() {
    }

    public Pedido(long id, String nome, String descricao, double valorTotal) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.valorTotal = valorTotal;
    }

    public Pedido(long id, String nome, String descricao, long horaQueFoiPedido) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.horaQueFoiPedido = horaQueFoiPedido;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public List<Produto> getItensDoPedido() {
        return itensDoPedido;
    }

    public void setItensDoPedido(Produto itensDoPedido) {
        this.itensDoPedido.add(itensDoPedido);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public long getMaiorTempo() {
        return maiorTempo;
    }

    public void setMaiorTempo(long maiorTempo) {
        this.maiorTempo = maiorTempo;
    }

    public long getTempoIdeal() {
        return tempoIdeal;
    }

    public void setTempoIdeal(long tempoIdeal) {
        this.tempoIdeal = tempoIdeal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public long getHoraQueFoiPedido() {
        return horaQueFoiPedido;
    }

    public void setHoraQueFoiPedido(long horaQueFoiPedido) {
        this.horaQueFoiPedido = horaQueFoiPedido;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
}
