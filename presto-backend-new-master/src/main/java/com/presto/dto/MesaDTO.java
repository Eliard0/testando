package com.presto.dto;

import com.presto.model.Mesa;
import com.presto.model.Pedido;


public class MesaDTO {
    private long id;
    private String nome;
    private Pedido pedido;
    private String status;
    private String nomeRestaurante;
    private UsuarioFrontDTO cliente;


    public MesaDTO(Mesa mesa) {
        this.id = mesa.getId();
        this.cliente = mesa.getCliente() != null? new UsuarioFrontDTO(mesa.getCliente()) : null;
        this.nome = mesa.getNome();
        this.status = mesa.getStatus();
        this.nomeRestaurante = mesa.getRestaurante() != null ? mesa.getRestaurante().getNome(): null;
        this.pedido = mesa.getPedido();
    }

    public MesaDTO() {
    }

    public MesaDTO(long id, String nome, Pedido pedido, String status, String nomeRestaurante, UsuarioFrontDTO cliente) {
        this.id = id;
        this.nome = nome;
        this.pedido = pedido;
        this.status = status;
        this.nomeRestaurante = nomeRestaurante;
        this.cliente = cliente;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    public void setNomeRestaurante(String nomeRestaurante) {
        this.nomeRestaurante = nomeRestaurante;
    }

    public UsuarioFrontDTO getCliente() {
        return cliente;
    }

    public void setCliente(UsuarioFrontDTO cliente) {
        this.cliente = cliente;
    }
}
