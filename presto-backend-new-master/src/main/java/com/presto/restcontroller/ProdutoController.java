package com.presto.restcontroller;

import com.presto.model.Produto;
import com.presto.model.Restaurante;
import com.presto.repository.ProdutoRepository;
import com.presto.repository.RestauranteRepository;
import com.presto.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("produto")
public class ProdutoController {
    @Autowired
    ProdutoRepository produtoRepository;
    @Autowired
    ProdutoService produtoService;

    @Autowired
    RestauranteRepository restauranteRepository;

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PostMapping("/create/{id}")
    ResponseEntity<?> createProduto(@ModelAttribute Produto produto, @RequestPart("file")MultipartFile file, @PathVariable("id") long id){
        try{
            Optional<Restaurante> restaurante = restauranteRepository.findById(id);
            if(restaurante.isPresent()){
                ResponseEntity<URI> statusImagem = produtoService.salvarImagem(file);
                if (statusImagem.getStatusCode().equals(HttpStatus.ACCEPTED) ) {
                    produto.setImagem("" + statusImagem.getBody());
                    produto.setUsuarioProduto(restaurante.get());

                    produto = produtoRepository.save(produto);
                    restaurante.get().addProduto(produto);
                    restauranteRepository.save(restaurante.get());
                    return new ResponseEntity<>(produto, HttpStatus.CREATED);
                }
                return new ResponseEntity<>("falhou ao salvar imagem", HttpStatus.EXPECTATION_FAILED);
            }
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/produtos/{idRestaurante}")
    ResponseEntity<List<Produto>> getAllProdutos(@PathVariable("idRestaurante") Long idRestaurante){
        try{
            Optional<Restaurante> restaurante = this.restauranteRepository.findById(idRestaurante);
            if (restaurante.isPresent()) {

                List<Produto> produtos = new ArrayList<>(restaurante.get().getProduto());

                if (produtos.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(produtos, HttpStatus.OK);
            }
            return ResponseEntity.notFound().build();
            }

        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping("/gettipo/{idRestaurante}/{tipo}")
    ResponseEntity<List<Produto>> getProdutosByTipo(
            @PathVariable("idRestaurante") Long idRestaurante,
            @PathVariable("tipo") String tipo){
        try{
            Optional<Restaurante> restaurante = this.restauranteRepository.findById(idRestaurante);
            if (restaurante.isPresent()){
                List<Produto> produtos = new ArrayList<>(produtoRepository.findByTipo(tipo));
                produtos = produtos.stream().filter(a -> restaurante.get().getProduto().contains(a)).collect(Collectors.toList());
                if(produtos.isEmpty()){
                    return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(produtos, HttpStatus.OK);
            }

            return ResponseEntity.notFound().build();
            }

        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping("/getnome/{nome}/{idRestaurante}")
    ResponseEntity<Produto> getProdutoByNome( @PathVariable("idRestaurante") Long idRestaurante, @PathVariable("nome") String nome){
        try {
            Optional<Restaurante> restaurante = this.restauranteRepository.findById(idRestaurante);
            if (restaurante.isPresent()){
                Optional<Produto> produto = produtoRepository.findByNomeContaining(nome);
                if (produto.isPresent()){
                    if(restaurante.get().getProduto().contains(produto.get())){
                        return new ResponseEntity<>(produto.get(), HttpStatus.OK);
                    }
                }
            }
           return ResponseEntity.notFound().build();

        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping("/updatesemimagem/{idProduto}/{idRestaurante}")
    ResponseEntity<?> updateProdutoSemImagem(@RequestBody Produto produto, @PathVariable("idProduto") long idProduto,  @PathVariable("id") long idRestaurante){
        try{
            Optional<Restaurante> restaurante = this.restauranteRepository.findById(idRestaurante);
            Optional<Produto> produtoOp = produtoRepository.findById(idProduto);
            if (produtoOp.isPresent() && restaurante.isPresent()){
                Produto _produto = produtoOp.get();
                if(restaurante.get().getProduto().contains(_produto)){
                    if(produto != null) {
                        if(produto.getNome() != null && !produto.getNome().equals("")){_produto.setNome(produto.getNome());}
                        if (produto.getTipo() != null && !produto.getTipo().equals("")){_produto.setTipo(produto.getTipo());}
                        if (produto.getTempo() != null){ _produto.setTempo(produto.getTempo());}
                        if (produto.getValor() != 0.0 && produto.getValor() != 0){_produto.setValor(produto.getValor());}
                        if(produto.getQuantidadeEstoque() > 0 && produto.getQuantidadeEstoque() != null) {
                            _produto.setQuantidadeEstoque(_produto.getQuantidadeEstoque() + produto.getQuantidadeEstoque());}
                        if (produto.getImagem() != null && !produto.getImagem().equals("")){_produto.setImagem(produto.getImagem());}
                        if (produto.getDescricao() != null && !produto.getDescricao().equals("")){_produto.setDescricao(produto.getDescricao());}
                        return new ResponseEntity<>(produtoRepository.save(_produto), HttpStatus.OK);
                    }
                }

            }

            return new ResponseEntity<>("Produto nulo", HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping("/update/{idProduto}/{idRestaurante}")
    ResponseEntity<?> updateProduto(
            @ModelAttribute Produto produto,
            @RequestPart("file")MultipartFile file,
            @PathVariable("idProduto") long idProduto,
            @PathVariable("id") long idRestaurante){
        try{
            Optional<Restaurante> restaurante = this.restauranteRepository.findById(idRestaurante);
            Optional<Produto> _produto = produtoRepository.findById(idProduto);
                if (produto != null && _produto.isPresent() && restaurante.isPresent()) {
                   if (restaurante.get().getProduto().contains(_produto.get())){
                       if (file != null) {
                           ResponseEntity<?> statusImagem = produtoService.salvarImagem(file);
                           if (statusImagem.getStatusCode() == HttpStatus.ACCEPTED) {
                               produto.setImagem("" + file.getOriginalFilename());
                           }
                       }

                       if (produto.getNome() != null) {
                           _produto.get().setNome(produto.getNome());
                       }
                       if (produto.getTipo() != null) {
                           _produto.get().setTipo(produto.getTipo());
                       }
                       if (produto.getTempo() != null) {
                           _produto.get().setTempo(produto.getTempo());
                       }
                       if (produto.getValor() == 0.0) {
                           _produto.get().setValor(produto.getValor());
                       }
                       if (produto.getQuantidadeEstoque() > 0 && produto.getQuantidadeEstoque() != null) {
                           _produto.get().setQuantidadeEstoque(_produto.get().getQuantidadeEstoque() + produto.getQuantidadeEstoque());
                       }
                       if (produto.getImagem() != null) {
                           _produto.get().setImagem(produto.getImagem());
                       }
                       if (produto.getDescricao() != null) {
                           _produto.get().setDescricao(produto.getDescricao());
                       }
                       return new ResponseEntity<>(produtoRepository.save(_produto.get()), HttpStatus.OK);
                   }
                   ResponseEntity.badRequest().build();
                }

            return new ResponseEntity<>("falhou ao salvar imagem", HttpStatus.EXPECTATION_FAILED);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteProduto( @PathVariable("id") Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isPresent()) {
            produtoRepository.delete(produto.get());
            return new ResponseEntity<>(produto, HttpStatus.GONE);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @GetMapping("/produtousuario/{idRestaurante}")
    public ResponseEntity<?> getProdutoRestaurante(@PathVariable("idRestaurante") long id){
        Optional<Restaurante> restaurante = restauranteRepository.findById(id);
        if (restaurante.isPresent()){
            List<Produto> produtos = restaurante.get().getProduto();
            return new ResponseEntity<>(produtos, HttpStatus.OK);
        }
       return ResponseEntity.notFound().build();
    }

}
