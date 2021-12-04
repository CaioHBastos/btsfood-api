package br.com.btstech.btsfoodapi.api.openapi.controller;

import br.com.btstech.btsfoodapi.api.exceptionhandler.Problem;
import br.com.btstech.btsfoodapi.api.model.PedidoModel;
import br.com.btstech.btsfoodapi.api.model.PedidoResumoModel;
import br.com.btstech.btsfoodapi.api.model.input.PedidoInput;
import br.com.btstech.btsfoodapi.domain.filter.PedidoFilter;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Api(tags = "Pedidos")
public interface PedidoControllerOpenApi {

    @ApiImplicitParams({
            @ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
                    name = "campos", paramType = "query", type = "string")
    })
    @ApiOperation("Pesquisa os pedidos")
    Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable);

    @ApiOperation("Registra um pedido")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Pedido registrado"),
    })
    ResponseEntity<PedidoModel> adicionar(
            @ApiParam(name = "corpo", value = "Representação de um novo pedido", required = true)
                    PedidoInput pedidoInput);

    @ApiImplicitParams({
            @ApiImplicitParam(value = "Nomes das propriedades para filtrar na resposta, separados por vírgula",
                    name = "campos", paramType = "query", type = "string")
    })
    @ApiOperation("Busca um pedido por código")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Pedido não encontrado", response = Problem.class)
    })
    ResponseEntity<PedidoModel> buscar(
            @ApiParam(value = "Código de um pedido", example = "f9981ca4-5a5e-4da3-af04-933861df3e55", required = true)
                    String codigoPedido);

}
