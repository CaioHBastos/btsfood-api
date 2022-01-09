package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.v1.openapi.controller.FluxoPedidoControllerOpenApi;
import br.com.btstech.btsfoodapi.domain.service.FluxoPedidoService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/pedidos/{codigoPedido}", produces = MediaType.APPLICATION_JSON_VALUE)
public class FluxoPedidoController implements FluxoPedidoControllerOpenApi {

    private FluxoPedidoService fluxoPedidoService;

    @PutMapping("/confirmacao")
    public ResponseEntity<Void> confirmar(@PathVariable String codigoPedido) {
        fluxoPedidoService.confirmar(codigoPedido);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancelamento")
    public ResponseEntity<Void> cancelar(@PathVariable String codigoPedido) {
        fluxoPedidoService.cancelar(codigoPedido);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/entrega")
    public ResponseEntity<Void> entregar(@PathVariable String codigoPedido) {
        fluxoPedidoService.entregar(codigoPedido);

        return ResponseEntity.noContent().build();
    }
}
