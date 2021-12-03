package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.CidadeInputDisassembler;
import br.com.btstech.btsfoodapi.api.assembler.CidadeModelAssembler;
import br.com.btstech.btsfoodapi.api.model.CidadeModel;
import br.com.btstech.btsfoodapi.api.model.input.CidadeInput;
import br.com.btstech.btsfoodapi.domain.exception.EstadoNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.exception.NegocioException;
import br.com.btstech.btsfoodapi.domain.model.Cidade;
import br.com.btstech.btsfoodapi.domain.repository.CidadeRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroCidadeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Cidades")
@AllArgsConstructor
@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

    private CidadeRepository cidadeRepository;
    private CadastroCidadeService cidadeCadastroService;
    private CidadeModelAssembler cidadeModelAssembler;
    private CidadeInputDisassembler cidadeInputDisassembler;

    @ApiOperation("Lista as cidades")
    @GetMapping
    public List<CidadeModel> listar() {
        List<Cidade> todasCidades = cidadeRepository.findAll();

        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }

    @ApiOperation("Busca uma cidade por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CidadeModel> buscar(@ApiParam(value = "Id de uma cidade", example = "1")
                                              @PathVariable Long id) {

        Cidade cidade = cidadeCadastroService.buscarOuFalhar(id);
        CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);

        return ResponseEntity.ok(cidadeModel);
    }

    @ApiOperation("Cadastra uma cidade")
    @PostMapping
    public ResponseEntity<CidadeModel> adicionar(@ApiParam(name = "corpo", value = "Representação de uma nova cidade")
                                                 @RequestBody @Valid CidadeInput novaCidade) {

        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(novaCidade);
            cidade = cidadeCadastroService.salvar(cidade);
            CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);

            return ResponseEntity.status(HttpStatus.CREATED).body(cidadeModel);

        } catch (EstadoNaoEncontradaException exception) {
            throw new NegocioException(exception.getMessage(), exception);
        }
    }

    @ApiOperation("Atualiza uma cidade por ID")
    @PutMapping("/{id}")
    public ResponseEntity<CidadeModel> atualizar(@ApiParam(value = "Id de uma cidade", example = "1")
                                                 @PathVariable Long id,
                                                 @ApiParam(name = "corpo", value = "Representação de uma cidade com os novos dados")
                                                 @RequestBody @Valid CidadeInput novaCidade) {

        try {
            Cidade cidadeAtual = cidadeCadastroService.buscarOuFalhar(id);

            cidadeInputDisassembler.copyToDomainObject(novaCidade, cidadeAtual);
            Cidade cidadeSalva = cidadeCadastroService.salvar(cidadeAtual);

            CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidadeSalva);

            return ResponseEntity.ok(cidadeModel);

        } catch (EstadoNaoEncontradaException exception) {
            throw new NegocioException(exception.getMessage());
        }
    }

    @ApiOperation("Remove uma cidade por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@ApiParam(value = "Id de uma cidade", example = "1")
                                        @PathVariable Long id) {

        cidadeCadastroService.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
