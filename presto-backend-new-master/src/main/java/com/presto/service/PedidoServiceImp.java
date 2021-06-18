package com.presto.service;


import com.presto.model.Pedido;
import com.presto.model.Produto;

import com.presto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;


@Service
public class PedidoServiceImp implements PedidoService {

    @Autowired
    ProdutoRepository produtoRepository;

    @Override
    public Pedido addAoPedido(Pedido pedido, List<Produto> produtos) {

        for(Produto produto : produtos ) {
            produto = produtoRepository.findById(produto.getId()).get();
            produto.setContador(produto.getContador()+1);
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque()-1);
            produtoRepository.save(produto);

            pedido.setItensDoPedido(produto);

        }
        return pedido;
    }

    @Override
    public Double somarTotal(List<Produto> produtos) {
        if (produtos.isEmpty()){
            return null;
        }
        double precoTotal = 0.0;
        BigDecimal total;

        for(Produto produto : produtos){
            precoTotal= precoTotal + produto.getValor();
            System.out.println(produto.getValor());
        }
        total= new BigDecimal(precoTotal, MathContext.DECIMAL64);
        precoTotal = total.doubleValue();
        return precoTotal;
    }

    @Override
    public long pegarMaiorTempo(List<Produto> produtos) {
        long maiorTempo = 0;
        for (Produto produto : produtos){

            if(produto.getTempo() > maiorTempo){
                maiorTempo = produto.getTempo();
            }
        }
        return maiorTempo;
    }

    @Override
    public Pedido atualizarItens(Pedido pedido, List<Produto> produtos) {

        List<Produto> aux = pedido.getItensDoPedido();
        for (Produto prod : aux) {

            prod = produtoRepository.findById(prod.getId()).get();
            prod.setContador(prod.getContador() - 1);
            prod.setQuantidadeEstoque(prod.getQuantidadeEstoque() + 1);

            produtoRepository.save(prod);
        }

        pedido = this.addAoPedido(pedido, produtos);
        return pedido;
    }
}
