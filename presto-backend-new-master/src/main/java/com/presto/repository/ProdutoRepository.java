package com.presto.repository;

import com.presto.model.Produto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepositoryImplementation<Produto, Long> {

    List<Produto> findByTipo(String tipo);
    Optional<Produto> findByNomeContaining(String nome);
    @Query("select distinct p from Produto p where p.nome like %:nome% and p.cardapio_id = :id")
    List<Produto> produtoPorNomeDeCardapio(@Param("nome")String nome, @Param("id") long id);
}
