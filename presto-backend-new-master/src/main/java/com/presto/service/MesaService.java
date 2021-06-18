package com.presto.service;

import com.presto.model.Mesa;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MesaService {

    ResponseEntity<?> removePedido(long idMesa, long idPedido);
    List<Mesa> ordenarMesaParaCozinha(List<Mesa> mesas);
}
