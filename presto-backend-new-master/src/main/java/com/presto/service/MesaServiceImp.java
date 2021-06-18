package com.presto.service;

import com.presto.model.Mesa;
import com.presto.model.Pedido;
import com.presto.repository.MesaRepository;
import com.presto.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MesaServiceImp implements MesaService{

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public ResponseEntity<?> removePedido(long idMesa, long idPedido) {
        Optional<Mesa> mesaOptional = mesaRepository.findById(idMesa);
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(idPedido);
        if (mesaOptional.isPresent() && pedidoOptional.isPresent()) {
            Mesa mesa = mesaOptional.get();
            Pedido pedido = pedidoOptional.get();
            if (mesa.getPedido() != null) {
                if (mesa.getPedido().getId() == pedido.getId()){
                    mesa.setPedido(null);
                    mesa.setStatus("LIVRE");
                    mesaRepository.save(mesa);
                    pedido.setMesa(null);
                    pedidoRepository.save(pedido);

                    return new ResponseEntity<>("Pedido removido com sucesso", HttpStatus.OK);
                }

            }
            return new ResponseEntity<>("A mesa não possui pedidos", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Mesa não existente", HttpStatus.NOT_FOUND);
    }
    @Override
    public List<Mesa> ordenarMesaParaCozinha(List<Mesa> mesas) {
        List<Mesa> aux = new ArrayList<>();
        for (Mesa mesa: mesas) {
            if (mesa.getPedido() != null) {
                aux.add(mesa);
            }
        }
        Collections.sort(aux, new Comparator<Mesa>() {
            @Override
            public int compare(Mesa mesa1, Mesa mesa2) {

                Pedido pedidoMesa1 = mesa1.getPedido();
                Pedido pedidoMesa2 = mesa2.getPedido();
                return pedidoMesa1.getHoraQueFoiPedido()< pedidoMesa2.getHoraQueFoiPedido() ? -1 : (pedidoMesa1.getHoraQueFoiPedido()> pedidoMesa2.getHoraQueFoiPedido() ? +1: 0);
            }
        });
        System.out.println(aux);
        return aux;
    }
}
