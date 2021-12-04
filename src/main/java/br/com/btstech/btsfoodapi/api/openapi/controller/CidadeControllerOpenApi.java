package br.com.btstech.btsfoodapi.api.openapi.controller;

import br.com.btstech.btsfoodapi.api.exceptionhandler.Problem;
import br.com.btstech.btsfoodapi.api.model.CidadeModel;
import br.com.btstech.btsfoodapi.api.model.input.CidadeInput;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Cidades")
public interface CidadeControllerOpenApi {

    @ApiOperation("Lista as cidades")
    List<CidadeModel> listar();

    @ApiOperation("Busca uma cidade por ID")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID da cidade inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    ResponseEntity<CidadeModel> buscar(@ApiParam(value = "Id de uma cidade", example = "1", required = true)
                                              Long id);

    @ApiOperation("Cadastra uma cidade")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cidade cadastrada")
    })
    ResponseEntity<CidadeModel> adicionar(@ApiParam(name = "corpo", value = "Representação de uma nova cidade", required = true)
                                                 CidadeInput novaCidade);

    @ApiOperation("Atualiza uma cidade por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cidade atualizada"),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    ResponseEntity<CidadeModel> atualizar(@ApiParam(value = "Id de uma cidade", example = "1", required = true)
                                                 Long id,
                                                 @ApiParam(name = "corpo", value = "Representação de uma cidade com os novos dados")
                                                 CidadeInput novaCidade) ;

    @ApiOperation("Remove uma cidade por ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cidade excluida"),
            @ApiResponse(code = 404, message = "Cidade não encontrada", response = Problem.class)
    })
    ResponseEntity<Void> remover(@ApiParam(value = "Id de uma cidade", example = "1", required = true)
                                        Long id) ;
}
