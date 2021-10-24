package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.PedidoInputDisassembler;
import br.com.btstech.btsfoodapi.api.assembler.PedidoModelAssembler;
import br.com.btstech.btsfoodapi.api.assembler.PedidoResumoModelAssembler;
import br.com.btstech.btsfoodapi.api.model.PedidoModel;
import br.com.btstech.btsfoodapi.api.model.PedidoResumoModel;
import br.com.btstech.btsfoodapi.api.model.input.PedidoInput;
import br.com.btstech.btsfoodapi.domain.exception.EntidadeNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.exception.NegocioException;
import br.com.btstech.btsfoodapi.domain.model.Pedido;
import br.com.btstech.btsfoodapi.domain.model.Usuario;
import br.com.btstech.btsfoodapi.domain.repository.PedidoRepository;
import br.com.btstech.btsfoodapi.domain.service.EmissaoPedidoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

    private PedidoRepository pedidoRepository;
    private EmissaoPedidoService emissaoPedido;
    private PedidoModelAssembler pedidoModelAssembler;
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;
    private PedidoInputDisassembler pedidoInputDisassembler;

    @GetMapping
    public ResponseEntity<List<PedidoResumoModel>> listar() {
        List<Pedido> todosPedidos = pedidoRepository.findAll();
        List<PedidoResumoModel> pedidoModels = pedidoResumoModelAssembler.toCollectionModel(todosPedidos);

        return ResponseEntity.ok(pedidoModels);
    }

    @GetMapping("/{codigoPedido}")
    public ResponseEntity<PedidoModel> buscar(@PathVariable String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        PedidoModel pedidoModel = pedidoModelAssembler.toModel(pedido);

        return ResponseEntity.ok(pedidoModel);
    }

    @PostMapping
    public ResponseEntity<PedidoModel> adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);

            novoPedido = emissaoPedido.emitir(novoPedido);
            PedidoModel pedidoModel = pedidoModelAssembler.toModel(novoPedido);

            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoModel);

        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
}
