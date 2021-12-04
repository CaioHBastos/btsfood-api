package br.com.btstech.btsfoodapi.api.controller.openapi;

import br.com.btstech.btsfoodapi.api.exceptionhandler.Problem;
import br.com.btstech.btsfoodapi.api.model.GrupoModel;
import br.com.btstech.btsfoodapi.api.model.input.GrupoInput;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Grupos")
public interface GrupoControllerOpenApi {

    @ApiOperation("Lista os grupos")
    List<GrupoModel> listar();

    @ApiOperation("Busca um grupo por ID")
    @ApiResponses({
            @ApiResponse(code = 400, message = "ID da grupo inválido", response = Problem.class),
            @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
    })
    GrupoModel buscar(
            @ApiParam(value = "ID de um grupo", example = "1")
                    Long grupoId);

    @ApiOperation("Cadastra um grupo")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Grupo cadastrado"),
    })
    ResponseEntity<GrupoModel> adicionar(
            @ApiParam(name = "corpo", value = "Representação de um novo grupo")
                    GrupoInput grupoInput);

    @ApiOperation("Atualiza um grupo por ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Grupo atualizado"),
            @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
    })
    GrupoModel atualizar(
            @ApiParam(value = "ID de um grupo", example = "1")
                    Long grupoId,

            @ApiParam(name = "corpo", value = "Representação de um grupo com os novos dados")
                    GrupoInput grupoInput);

    @ApiOperation("Exclui um grupo por ID")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Grupo excluído"),
            @ApiResponse(code = 404, message = "Grupo não encontrado", response = Problem.class)
    })
    ResponseEntity<Void> remover(
            @ApiParam(value = "ID de um grupo", example = "1")
                    Long grupoId);

}
