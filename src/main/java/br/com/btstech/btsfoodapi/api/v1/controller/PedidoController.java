package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.v1.assembler.PedidoInputDisassembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.PedidoModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.PedidoResumoModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.model.PedidoModel;
import br.com.btstech.btsfoodapi.api.v1.model.PedidoResumoModel;
import br.com.btstech.btsfoodapi.api.v1.model.input.PedidoInput;
import br.com.btstech.btsfoodapi.api.v1.openapi.controller.PedidoControllerOpenApi;
import br.com.btstech.btsfoodapi.core.data.PageWrapper;
import br.com.btstech.btsfoodapi.core.data.PageableTranslator;
import br.com.btstech.btsfoodapi.core.security.BtsSecurity;
import br.com.btstech.btsfoodapi.core.security.CheckSecurity;
import br.com.btstech.btsfoodapi.domain.exception.EntidadeNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.exception.NegocioException;
import br.com.btstech.btsfoodapi.domain.filter.PedidoFilter;
import br.com.btstech.btsfoodapi.domain.model.Pedido;
import br.com.btstech.btsfoodapi.domain.model.Usuario;
import br.com.btstech.btsfoodapi.domain.repository.PedidoRepository;
import br.com.btstech.btsfoodapi.domain.service.EmissaoPedidoService;
import br.com.btstech.btsfoodapi.infrastructure.repository.spec.PedidoSpecs;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/v1/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

    private PedidoRepository pedidoRepository;
    private EmissaoPedidoService emissaoPedido;
    private PedidoModelAssembler pedidoModelAssembler;
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;
    private PedidoInputDisassembler pedidoInputDisassembler;
    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;
    private BtsSecurity btsSecurity;


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

    @CheckSecurity.Pedidos.PodePesquisar
    @Override
    @GetMapping
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable) {
        Pageable pageableTraduzido = traduzirPageable(pageable);

        Page<Pedido> pedidosPage = pedidoRepository.findAll(
                PedidoSpecs.usandoFiltro(filtro), pageableTraduzido);

        pedidosPage = new PageWrapper<>(pedidosPage, pageable);

        return pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);
    }

    @CheckSecurity.Pedidos.PodeBuscar
    @Override
    @GetMapping("/{codigoPedido}")
    public ResponseEntity<PedidoModel> buscar(@PathVariable String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        PedidoModel pedidoModel = pedidoModelAssembler.toModel(pedido);

        return ResponseEntity.ok(pedidoModel);
    }

    @Override
    @PostMapping
    public ResponseEntity<PedidoModel> adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(btsSecurity.getUsuarioId());

            novoPedido = emissaoPedido.emitir(novoPedido);
            PedidoModel pedidoModel = pedidoModelAssembler.toModel(novoPedido);

            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoModel);

        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    private Pageable traduzirPageable(Pageable apiPageable) {
        var mapeamento = Map.of(
                "codigo", "codigo",
                "subtotal", "subtotal",
                "taxaFrete", "taxaFrete",
                "valorTotal", "valorTotal",
                "dataCriacao", "dataCriacao",
                "nomerestaurante", "restaurante.nome",
                "restaurante.id", "restaurante.id",
                "cliente.id", "cliente.id",
                "cliente.nome", "cliente.nome"
        );

        return PageableTranslator.translate(apiPageable, mapeamento);
    }
}
