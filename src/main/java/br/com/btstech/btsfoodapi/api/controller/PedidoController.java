package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.PedidoModelAssembler;
import br.com.btstech.btsfoodapi.api.assembler.PedidoResumoModelAssembler;
import br.com.btstech.btsfoodapi.api.model.PedidoModel;
import br.com.btstech.btsfoodapi.api.model.PedidoResumoModel;
import br.com.btstech.btsfoodapi.domain.model.Pedido;
import br.com.btstech.btsfoodapi.domain.repository.PedidoRepository;
import br.com.btstech.btsfoodapi.domain.service.EmissaoPedidoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

    private PedidoRepository pedidoRepository;
    private EmissaoPedidoService emissaoPedido;
    private PedidoModelAssembler pedidoModelAssembler;
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;

    @GetMapping
    public ResponseEntity<List<PedidoResumoModel>> listar() {
        List<Pedido> todosPedidos = pedidoRepository.findAll();
        List<PedidoResumoModel> pedidoModels = pedidoResumoModelAssembler.toCollectionModel(todosPedidos);

        return ResponseEntity.ok(pedidoModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoModel> buscar(@PathVariable("id") Long pedidoId) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);
        PedidoModel pedidoModel = pedidoModelAssembler.toModel(pedido);

        return ResponseEntity.ok(pedidoModel);
    }
}
