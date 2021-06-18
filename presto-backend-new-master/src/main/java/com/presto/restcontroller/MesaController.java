package com.presto.restcontroller;

import com.presto.dto.MesaDTO;
import com.presto.model.Mesa;
import com.presto.model.Pedido;
import com.presto.model.Restaurante;
import com.presto.model.Usuario;
import com.presto.repository.MesaRepository;
import com.presto.repository.PedidoRepository;

import com.presto.repository.RestauranteRepository;
import com.presto.repository.UsuarioRepository;
import com.presto.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("mesa")
@RestController
public class MesaController {
    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MesaService mesaService;
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PostMapping("/create/{id}")
    public ResponseEntity<?> criarMesa(@RequestBody Mesa mesa, @PathVariable("id") long id){
        Restaurante restaurante = restauranteRepository.findById(id).get();
        if(mesa != null && restaurante != null){
            mesa.setRestaurante(restaurante);
            mesa.setStatus("LIVRE");
            mesa = mesaRepository.save(mesa);
            restaurante.addMesa(mesa);
            restauranteRepository.save(restaurante);
            return new ResponseEntity<>(mesa, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/mesas")
    public ResponseEntity<List<Mesa>> getAllMesas(){
        List<Mesa> mesas = new ArrayList<>(mesaRepository.findAll());
        return new ResponseEntity<>(mesas, HttpStatus.OK);
    }

    @PutMapping("/addpedido/{id}")
    public ResponseEntity<?> addPedido(@PathVariable("id") long id, @RequestBody Pedido pedido){

        Optional<Mesa> mesa = mesaRepository.findById(id);

        if (mesa.isPresent()){
            pedido.setMesa(mesa.get());
            Pedido pedido1 = pedidoRepository.save(pedido);

            mesa.get().setPedido(pedido);
            mesaRepository.save(mesa.get());
            return new ResponseEntity<>(pedido1,HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    
    
    
    @PutMapping("/createpedido/{idMesa}")
    public ResponseEntity<?> createAddPedido(@PathVariable("idMesa") long id){

        Optional<Mesa> mesa = mesaRepository.findById(id);
        Pedido pedido = new Pedido();
        if (mesa.isPresent()){
            pedido.setMesa(mesa.get());
            pedido.setNome("Pedido "+ mesa.get().getNome());
            Pedido pedido1 = pedidoRepository.save(pedido);

            mesa.get().setPedido(pedido);
            mesaRepository.save(mesa.get());
            return new ResponseEntity<>(pedido1,HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/removepedido/{idMesa}/{idPedido}")
    public ResponseEntity<?> removePedido(@PathVariable("idMesa") long idMesa, @PathVariable("idPedido") long idPedido) {
        ResponseEntity<?> retorno = mesaService.removePedido(idMesa, idPedido);
        return new ResponseEntity<>(retorno.getBody(), retorno.getStatusCode());
    }

    @GetMapping("/getpedidomesa/{id}")
    public ResponseEntity<?> getPedidoDaMesa(@PathVariable("id") long id){
        Mesa mesa = mesaRepository.findById(id).get();
        if (mesa != null) {
            Pedido pedido = mesa.getPedido();
            return new ResponseEntity<>(pedido, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/mesarestaurante/{id}")
    public ResponseEntity<?> getMesaUsuario(@PathVariable("id") long id){
        Optional<Restaurante> restaurante = restauranteRepository.findById(id);
        if (restaurante.isPresent()) {
            List<Mesa> mesas = restaurante.get().getMesa();
            List<MesaDTO> mesaDTOS = new ArrayList<>();
            mesas.forEach(mesa -> mesaDTOS.add(new MesaDTO(mesa)));
            return new ResponseEntity<>(mesaDTOS, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/mesasbytime/{id}")
    public ResponseEntity<?> mesasBytempo(@PathVariable("id") long id){
        List<Mesa> mesas = new ArrayList<>();
        Restaurante restaurante = restauranteRepository.findById(id).get();
        if (restaurante != null) {
            restaurante.getMesa().forEach(mesas::add);
            mesas = mesaService.ordenarMesaParaCozinha(mesas);
            return new ResponseEntity<>(mesas, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/mesaadd/{id}")
    public ResponseEntity<?> mesaAdd(@PathVariable("id") long id, @RequestBody Mesa mesa){
        Optional<Restaurante> restaurante = restauranteRepository.findById(id);

        if (restaurante.isPresent()){
            mesa.setRestaurante(restaurante.get());
            Mesa mesa1 = mesaRepository.save(mesa);

            restaurante.get().addMesa(mesa1);
            restauranteRepository.save(restaurante.get());
            return new ResponseEntity<>(mesa1, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/diminuirtempo/{id}")
    public ResponseEntity<?> diminuirTempo(@PathVariable("id") long id){
        Restaurante restaurante = restauranteRepository.findById(id).get();
        List<Mesa> mesaList = restaurante.getMesa();

        for (Mesa mesa: mesaList) {
            Pedido pedido = mesa.getPedido();
            if (pedido != null && pedido.getMaiorTempo() > 0) {
                pedido.setMaiorTempo(pedido.getMaiorTempo() - 1);
                pedidoRepository.save(pedido);
            }

        }
        return new ResponseEntity<>(mesaList, HttpStatus.OK);
    }

    @PutMapping("/addcliente/{idMesa}/{email}/{idRestaurante}")
    public ResponseEntity<?> addCliente(@PathVariable("email") String email,
                                        @PathVariable("idRestaurante") Long idRestaurante,
                                        @PathVariable("idMesa") Long idMesa
                                        ){
        Optional<Restaurante> restaurante = restauranteRepository.findById(idRestaurante);

        Optional<Usuario> usuario = this.usuarioRepository.findByEmail(email);
        if (restaurante.isPresent() && usuario.isPresent()){
            List<Mesa> mesaList = restaurante.get().getMesa();

            if (!mesaList.isEmpty() &&
                    usuario.get().getRestaurantes().stream().anyMatch(r -> r.getId().equals(restaurante.get().getId()))
            ){
                Optional<Mesa> mesa = mesaList.stream().filter(m -> m.getId() == idMesa).findFirst();
                mesa.ifPresent(value -> {
                    value.setCliente(usuario.get());
                    value.setStatus("OCUPADA");
                    usuario.get().setMesa(value);
                    this.usuarioRepository.save(usuario.get());
                    this.mesaRepository.save(value);
                });
                if (mesa.isPresent()){
                    return ResponseEntity.ok(new MesaDTO(mesa.get()));
                }else {
                    return ResponseEntity.notFound().build();
                }

            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("chamargarcom/{idMesa}")
    public ResponseEntity<?> chamarGarcom(@PathVariable("idMesa") Long idMesa){
        Optional<Mesa> mesaOptional = this.mesaRepository.findById(idMesa);
        if(mesaOptional.isPresent()){
            mesaOptional.get().setStatus("CHAMANDO GARCOM");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("clienteatendido/{idMesa}")
    public ResponseEntity<?> clienteAtendido(@PathVariable("idMesa") Long idMesa){
        Optional<Mesa> mesaOptional = this.mesaRepository.findById(idMesa);
        if(mesaOptional.isPresent()){
            mesaOptional.get().setStatus("PEDIDO EM ANDAMENTO");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
