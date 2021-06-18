package com.presto.restcontroller;

import com.presto.model.Cardapio;
import com.presto.model.Produto;
import com.presto.model.Restaurante;
import com.presto.repository.CardapioRepository;
import com.presto.repository.ProdutoRepository;
import com.presto.repository.RestauranteRepository;
import com.presto.service.CardapioServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("cardapio")
public class CardapioController {
    @Autowired
    CardapioRepository cardapioRepository;


    @Autowired 
    CardapioServiceImp cardapioServiceImp;
    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;


    @GetMapping("/getfiltro/{id}/{tipo}")
    public ResponseEntity<?> getfiltro(@PathVariable("id") long id, @PathVariable("tipo") String tipo) {

        ResponseEntity<?> cardapio1 = cardapioServiceImp.filtraProdutos(tipo, id);
        if (cardapio1.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>(cardapio1.getBody(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/cardapios")
    public ResponseEntity<List<Cardapio>> getAllCardapios() {
        try {
            List<Cardapio> cardapios = new ArrayList<Cardapio>();
            cardapioRepository.findAll().forEach(cardapios::add);

            if (cardapios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(cardapios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getCardapioById(@PathVariable("id") long id) {
        Restaurante restaurante = restauranteRepository.findById(id).get();

        Cardapio cardapioData = restaurante.getCardapio();
        return new ResponseEntity<>(cardapioData, HttpStatus.OK);
    }


    @GetMapping("/getnome/{nome}")
    public ResponseEntity<Cardapio> getCardapioByNome(@PathVariable("nome") String nome) {
        Optional<Cardapio> cardapioData = cardapioRepository.findByNomeContaining(nome);
        if (cardapioData.isPresent()) {
            return new ResponseEntity<>(cardapioData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PostMapping("/create/{id}")
    public ResponseEntity<Cardapio> createCardapio(@RequestBody Cardapio cardapio, @PathVariable("id") long id) {
        try {
            Restaurante restaurante = restauranteRepository.findById(id).get();
            if(restaurante != null) {
                cardapio.setUsuario(restaurante);
               cardapio = cardapioRepository.save(cardapio);
               restaurante.addCardapio(cardapio);
               restauranteRepository.save(restaurante);
                return new ResponseEntity<>(cardapio, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping("/updatebyid/{id}")
    public ResponseEntity<Cardapio> updateCardapio(@PathVariable("id") long id, @RequestBody Cardapio cardapio) {
        Optional<Cardapio> cardapioData = cardapioRepository.findById(id);

        if (cardapioData.isPresent()) {
            Cardapio _cardapio = cardapio;
           _cardapio.setNome(cardapio.getNome());
            cardapioRepository.save(_cardapio);
            return new ResponseEntity<>(_cardapio, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping("/addproduto/{id}")
    public ResponseEntity<?> updateCardapioPorNome(@PathVariable("id") long id, @RequestBody Produto produto) {
        try {
            Restaurante restaurante = restauranteRepository.findById(id).get();
            Cardapio cardapioData = restaurante.getCardapio();

            if (cardapioData != null) {
                produto= produtoRepository.findById(produto.getId()).get();
                cardapioData.setProdutos(produto);
                produto.setCardapios(cardapioData);
                cardapioRepository.save(cardapioData);
                produtoRepository.save(produto);
                return new ResponseEntity<>(produto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping("/remove/{id}")
    public ResponseEntity<?> removeProduto(@PathVariable("id") long id, @RequestBody Produto produto) {
        try {
            produto= produtoRepository.findById(produto.getId()).get();
            Cardapio cardapioData = cardapioServiceImp.removerProduto(id, produto);
                if(cardapioData != null){
                    produto.setCardapios(null);
                    produtoRepository.save(produto);
                return new ResponseEntity<>(cardapioData, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(cardapioData, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteCardapio(@PathVariable("id") long id) {
        try {
           cardapioRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteAllCardapios() {
        try {
            cardapioRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

    }


    @GetMapping("/cardapiousuario/{id}")
    public ResponseEntity<?> getCardapioUsuario(@PathVariable("id") long id){
        Restaurante restaurante = restauranteRepository.findById(id).get();
        if (restaurante != null) {
            Cardapio cardapio = restaurante.getCardapio();
            return new ResponseEntity<>(cardapio, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getprodutoscardapio/{id}")
    public ResponseEntity<?> getProdutosCardapio(@PathVariable("id") long id) {
        Restaurante restaurante = restauranteRepository.findById(id).get();

        Cardapio cardapioData = restaurante.getCardapio();
        cardapioData.setProdutos(cardapioServiceImp.produtosCardapio(cardapioData.getProdutos()));
        cardapioRepository.save(cardapioData);
        return new ResponseEntity<>(cardapioData.getProdutos(), HttpStatus.OK);
    }
    @GetMapping("/produtoscardapiobyname/{id}/{nome}")
    public ResponseEntity<?> produtosCardapioByName(@PathVariable("id") long id, @PathVariable("nome") String nome) {
        Restaurante restaurante = restauranteRepository.findById(id).get();

        Cardapio cardapioData = restaurante.getCardapio();
        List<Produto> produtos = produtoRepository.produtoPorNomeDeCardapio(nome, cardapioData.getId());

        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }


}
