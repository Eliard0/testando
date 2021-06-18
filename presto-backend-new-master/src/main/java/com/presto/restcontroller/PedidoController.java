package com.presto.restcontroller;

import com.presto.model.Mesa;
import com.presto.model.Pedido;
import com.presto.model.Produto;
import com.presto.model.Usuario;
import com.presto.repository.MesaRepository;
import com.presto.repository.PedidoRepository;
import com.presto.repository.UsuarioRepository;
import com.presto.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("pedido")
@RestController
public class PedidoController {
    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    MesaRepository mesaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PedidoService pedidoService;

    @PostMapping("/create/{email}")
    public ResponseEntity<?> criarPedido(@RequestBody Pedido pedido, @PathVariable("email") String email){
        try {
            Optional<Usuario> user = this.usuarioRepository.findByEmail(email);

            if(!user.isPresent()){return ResponseEntity.notFound().build();}
            if (user.get().getMesa() == null){return ResponseEntity.badRequest().build();}
            Mesa mesa = user.get().getMesa();
            pedido.setMesa(mesa);
            pedido.setNomeCliente(mesa.getCliente().getNome());
            Pedido _pedido = pedidoRepository.save(pedido);
            mesa.setPedido(_pedido);
            mesa.setStatus("PEDIDO EM ANDAMENTO");
            this.mesaRepository.save(mesa);
            return new ResponseEntity<>(_pedido, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }

    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<Pedido>> getAllMesas(){
        List<Pedido> pedidos = new ArrayList<Pedido>();
        pedidoRepository.findAll().forEach(pedidos::add);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }


    @PutMapping("/addmesa/{id}")
    public ResponseEntity<?> addMesaById(@RequestBody Mesa mesa, @PathVariable("id") long id){
        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (pedido.isPresent()){
            pedido.get().setMesa(mesa);
            return new ResponseEntity<>(pedidoRepository.save(pedido.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/addprodutos/{id}")
    public ResponseEntity<?> addProdutos(@RequestBody List<Produto> produtos , @PathVariable("id") long id){
        Pedido pedido = pedidoRepository.findById(id).get();

        if (pedido != null){
            pedido = pedidoService.addAoPedido(pedido, produtos);
            pedido.setValorTotal(pedidoService.somarTotal(pedido.getItensDoPedido()));
            pedido.setMaiorTempo(pedidoService.pegarMaiorTempo(pedido.getItensDoPedido()));
            return new ResponseEntity<>(pedidoRepository.save(pedido), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @PutMapping("/updateitensdopedido/{id}")
    public ResponseEntity<?> atualizarProdutos(@RequestBody List<Produto> produtos , @PathVariable("id") long id){
        Pedido pedido = pedidoRepository.findById(id).get();

        if (pedido != null){
            pedido = pedidoService.addAoPedido(pedido, produtos);
            pedido.setValorTotal(pedidoService.somarTotal(pedido.getItensDoPedido()));
            pedido.setMaiorTempo(pedidoService.pegarMaiorTempo(pedido.getItensDoPedido()));
            return new ResponseEntity<>(pedidoRepository.save(pedido), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @PutMapping("/addtempo/{id}/{tempoideal}/{now}")
    public ResponseEntity<?> addTempo(@PathVariable("tempoideal")long tempoIdeal, @PathVariable("id") long id, @PathVariable("now") long now,@RequestBody Pedido pedido){
        Optional<Pedido> _pedido = pedidoRepository.findById(id);

        if (_pedido.isPresent()){
        	
        	_pedido.get().setDescricao(pedido.getDescricao());
            _pedido.get().setTempoIdeal(tempoIdeal);
            _pedido.get().setHoraQueFoiPedido(now);
            return new ResponseEntity<>(pedidoRepository.save(_pedido.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
  
    
}
