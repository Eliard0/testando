
package com.presto.service;

import com.presto.model.Cardapio;
import com.presto.model.Produto;
import com.presto.model.Restaurante;
import com.presto.repository.CardapioRepository;
import com.presto.repository.ProdutoRepository;
import com.presto.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CardapioServiceImp implements CardapioService{
    @Autowired
    CardapioRepository cardapioRepository;
    @Autowired
    RestauranteRepository restauranteRepository;
    @Autowired
    ProdutoRepository produtoRepository;
    @Override
    public ResponseEntity<?> filtraProdutos(String tipo, long id) {

        Restaurante restaurante = restauranteRepository.findById(id).get();
        Cardapio cardapio = restaurante.getCardapio();
        cardapio.setProdutos(this.produtosCardapio(cardapio.getProdutos()));
        cardapio = cardapioRepository.save(cardapio);
        List<Produto> listaProdutosRetorno = new ArrayList<Produto>();
            if (cardapio != null) {
                for (Produto produto : cardapio.getProdutos()) {
                    String aux = produto.getTipo();
                    if(aux.equals(tipo)) {
                        listaProdutosRetorno.add(produto);
                    }

                }
                return new ResponseEntity<>(listaProdutosRetorno, HttpStatus.OK);
            }
            return new ResponseEntity<>("Cardápio não encontrado", HttpStatus.NOT_FOUND);
    }



    @Override
    public Cardapio removerProduto(long idUsuario, Produto produto) {
        Optional<Restaurante> usuario = restauranteRepository.findById(idUsuario);
        Cardapio cardapio = usuario.get().getCardapio();

        if (usuario.isPresent()) {
            List<Produto> listaDeProdutosAux= new ArrayList<>();

            for (Produto _produto : cardapio.getProdutos()) {
                if (_produto.getId() != produto.getId()) {
                   listaDeProdutosAux.add(_produto);
                }
            }
            cardapio.setProdutos(listaDeProdutosAux);
            cardapioRepository.save(cardapio);
            return cardapio;
        }
        return null;
    }

    @Override
    public List<Produto> produtosCardapio(List<Produto> produtos) {
       List<Produto> aux = new ArrayList<>();
        for(Produto produto : produtos){
            if(produto.getQuantidadeEstoque()>1) {
                aux.add(produto);
            }else if(produto.getQuantidadeEstoque()<=1){
                produto.setCardapios(null);
                produtoRepository.save(produto);
            }
        }
       return aux;
    }
}