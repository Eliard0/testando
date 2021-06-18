package com.presto.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.presto.dto.UsuarioDTO;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
@Entity
public class Usuario implements UserDetails ,Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String nome;

    private String email;

    private String senha;

    private String dataNascimento;
    @ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="tipos_usuario")
	private Set<Integer> tipos = new HashSet<>();
    
    @ManyToMany(mappedBy = "usuarios", cascade = CascadeType.ALL)
    private List<Restaurante> restaurantes = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "cliente")
    private Mesa mesa;

    public Usuario() {
    }
    public Usuario(UsuarioDTO usuarioDTO) {
        this.id = usuarioDTO.getId();
        this.nome = usuarioDTO.getNome();
        this.email = usuarioDTO.getEmail();
        this.senha = usuarioDTO.getSenha();
        this.tipos.add(2);
        this.dataNascimento = usuarioDTO.getDataNascimento();
    }
    
    public Usuario(UsuarioDTO usuarioDTO, String ADMIM) {
        this.id = usuarioDTO.getId();
        this.nome = usuarioDTO.getNome();
        this.email = usuarioDTO.getEmail();
        this.senha = usuarioDTO.getSenha();
        this.tipos.add(1);
        this.dataNascimento = usuarioDTO.getDataNascimento();
    }



    public Usuario(long id, String nome, String email, String senha, String dataNascimento,
			List<Restaurante> restaurantes) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.dataNascimento = dataNascimento;
		this.tipos.add(1);
		this.restaurantes = restaurantes;
	}
	public List<Restaurante> getRestaurantes() {
        return restaurantes;
    }

    public void setRestaurantes(List<Restaurante> restaurantes) {
        this.restaurantes = restaurantes;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    


    public Set<Integer> getTipos() {
		return tipos;
	}
	public void setTipos(Set<Integer> tipos) {
		this.tipos = tipos;
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

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.tipos.stream().map(us -> new SimpleGrantedAuthority(TipoUsuario.toEnum(us).getDescricao())).collect(Collectors.toList());
	}
	@Override
	public String getPassword() {
	
		return this.senha;
	}
	@Override
	public String getUsername() {
		return this.email;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
