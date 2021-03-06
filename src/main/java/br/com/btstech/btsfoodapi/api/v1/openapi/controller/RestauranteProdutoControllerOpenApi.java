package br.com.btstech.btsfoodapi.api.v1.openapi.controller;

import br.com.btstech.btsfoodapi.api.exceptionhandler.Problem;
import br.com.btstech.btsfoodapi.api.v1.model.ProdutoModel;
import br.com.btstech.btsfoodapi.api.v1.model.input.ProdutoInput;
import br.com.btstech.btsfoodapi.api.v1.openapi.model.ProdutosModelOpenApi;
import io.swagger.annotations.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@Api(tags = "Produtos")
public interface RestauranteProdutoControllerOpenApi {

    @ApiOperation(value = "Lista os produtos de um restaurante", response = ProdutosModelOpenApi.class)
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID do restaurante inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<CollectionModel<ProdutoModel>> listar(
            @ApiParam(value = "ID do restaurante", example = "1", required = true)
                    Long restauranteId,

            @ApiParam(value = "Indica se deve ou não incluir produtos inativos no resultado da listagem",
                    example = "false", defaultValue = "false")
                    Boolean incluirInativos);

    @ApiOperation("Busca um produto de um restaurante")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID do restaurante ou produto inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Produto de restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<ProdutoModel> buscar(
            @ApiParam(value = "ID do restaurante", example = "1", required = true)
                    Long restauranteId,

            @ApiParam(value = "ID do produto", example = "1", required = true)
                    Long produtoId);

    @ApiOperation("Cadastra um produto de um restaurante")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Produto cadastrado"),
            @ApiResponse(code = 404, message = "Restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<ProdutoModel> adicionar(
            @ApiParam(value = "ID do restaurante", example = "1", required = true)
                    Long restauranteId,

            @ApiParam(name = "corpo", value = "Representação de um novo produto", required = true)
                    ProdutoInput produtoInput);

    @ApiOperation("Atualiza um produto de um restaurante")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Produto atualizado"),
            @ApiResponse(code = 404, message = "Produto de restaurante não encontrado", response = Problem.class)
    })
    ResponseEntity<ProdutoModel> atualizar(
            @ApiParam(value = "ID do restaurante", example = "1", required = true)
                    Long restauranteId,

            @ApiParam(value = "ID do produto", example = "1", required = true)
                    Long produtoId,

            @ApiParam(name = "corpo", value = "Representação de um produto com os novos dados",
                    required = true)
                    ProdutoInput produtoInput);
}
