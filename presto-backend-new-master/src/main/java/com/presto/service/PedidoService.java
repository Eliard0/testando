package com.presto.service;

import com.presto.model.Pedido;
import com.presto.model.Produto;


import java.util.List;

public interface PedidoService {
    Pedido addAoPedido(Pedido pedido, List<Produto> produtos);
    Double somarTotal(List<Produto> produtos);
    long pegarMaiorTempo(List<Produto> produtos);
    Pedido atualizarItens(Pedido pedido, List<Produto> produtos);

}
