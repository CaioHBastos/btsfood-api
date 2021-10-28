package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.PedidoInputDisassembler;
import br.com.btstech.btsfoodapi.api.assembler.PedidoModelAssembler;
import br.com.btstech.btsfoodapi.api.assembler.PedidoResumoModelAssembler;
import br.com.btstech.btsfoodapi.api.model.PedidoModel;
import br.com.btstech.btsfoodapi.api.model.PedidoResumoModel;
import br.com.btstech.btsfoodapi.api.model.input.PedidoInput;
import br.com.btstech.btsfoodapi.core.data.PageableTranslator;
import br.com.btstech.btsfoodapi.domain.exception.EntidadeNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.exception.NegocioException;
import br.com.btstech.btsfoodapi.domain.model.Pedido;
import br.com.btstech.btsfoodapi.domain.model.Usuario;
import br.com.btstech.btsfoodapi.domain.repository.PedidoRepository;
import br.com.btstech.btsfoodapi.domain.repository.filter.PedidoFilter;
import br.com.btstech.btsfoodapi.domain.service.EmissaoPedidoService;
import br.com.btstech.btsfoodapi.infrastructure.repository.spec.PedidoSpecs;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    /*@GetMapping
    public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
        List<Pedido> todosPedidos = pedidoRepository.findAll();
        List<PedidoResumoModel> pedidoModels = pedidoResumoModelAssembler.toCollectionModel(todosPedidos);

        MappingJacksonValue pedidoWrapper = new MappingJacksonValue(pedidoModels);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());

        if (StringUtils.isNotBlank(campos)) {
            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
        }

        pedidoWrapper.setFilters(filterProvider);

        return pedidoWrapper;
    }*/

    @GetMapping
    public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable) {
        pageable = traduzirPageable(pageable);

        Page<Pedido> pedidosPage = pedidoRepository.findAll(
                PedidoSpecs.usandoFiltro(filtro), pageable);

        List<PedidoResumoModel> pedidosResumoModel = pedidoResumoModelAssembler
                .toCollectionModel(pedidosPage.getContent());

        Page<PedidoResumoModel> pedidosResumoModelPage = new PageImpl<>(
                pedidosResumoModel, pageable, pedidosPage.getTotalElements());

        return pedidosResumoModelPage;
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

    private Pageable traduzirPageable(Pageable apiPageable) {
        var mapeamento = ImmutableMap.of(
                "codigo", "codigo",
                "restaurante.nome", "restaurante.nome",
                "cliente.nome", "cliente.nome",
                "valorTotal", "valorTotal"
        );

        return PageableTranslator.translate(apiPageable, mapeamento);
    }
}
