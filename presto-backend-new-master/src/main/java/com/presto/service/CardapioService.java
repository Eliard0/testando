package com.presto.service;


import com.presto.model.Cardapio;
import com.presto.model.Produto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CardapioService {

    ResponseEntity<?> filtraProdutos(String tipo,long id);
    Cardapio removerProduto(long id, Produto produto);
    List<Produto> produtosCardapio(List<Produto> produtos);
}
