package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.domain.service.FluxoPedidoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/pedidos/{id}")
public class FluxoPedidoController {

    private FluxoPedidoService fluxoPedidoService;

    @PutMapping("/confirmacao")
    public ResponseEntity<Void> confirmar(@PathVariable("id") Long pedidoId) {
        fluxoPedidoService.confirmar(pedidoId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancelamento")
    public ResponseEntity<Void> cancelar(@PathVariable("id") Long pedidoId) {
        fluxoPedidoService.cancelar(pedidoId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/entrega")
    public ResponseEntity<Void> entregar(@PathVariable("id") Long pedidoId) {
        fluxoPedidoService.entregar(pedidoId);

        return ResponseEntity.noContent().build();
    }
}
