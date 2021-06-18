package com.presto.model;



import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class Mesa implements Serializable {
   
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private  String nome;

    private String status;

    @OneToOne(mappedBy = "mesa")
    private Pedido pedido;

    @JsonIgnore
    @ManyToOne
    @JoinTable(name = "restaurante_mesas",
            joinColumns = @JoinColumn(name = "mesa_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurante_id"))
    private Restaurante mesaRestaurante;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "usuario_mesa",
            joinColumns = @JoinColumn(name = "mesa_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Usuario cliente;

    public Mesa() {
    }

    public Mesa(String nome, Pedido pedido) {
        this.nome = nome;
        this.pedido =  pedido;
    }

    public Mesa(long id, String nome) {
        this.id = id;
        this.nome = nome;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonIgnore
    public Restaurante getRestaurante() {
        return this.mesaRestaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.mesaRestaurante = restaurante;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }
}
