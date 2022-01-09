package br.com.btstech.btsfoodapi.api.v2.controller;

import br.com.btstech.btsfoodapi.api.ResourceUriHelper;
import br.com.btstech.btsfoodapi.api.v2.assembler.CidadeInputDisassemblerV2;
import br.com.btstech.btsfoodapi.api.v2.assembler.CidadeModelAssemblerV2;
import br.com.btstech.btsfoodapi.api.v2.model.CidadeModelV2;
import br.com.btstech.btsfoodapi.api.v2.model.input.CidadeInputV2;
import br.com.btstech.btsfoodapi.core.web.BtsMediaTypes;
import br.com.btstech.btsfoodapi.domain.exception.EstadoNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.exception.NegocioException;
import br.com.btstech.btsfoodapi.domain.model.Cidade;
import br.com.btstech.btsfoodapi.domain.repository.CidadeRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroCidadeService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/cidades", produces = BtsMediaTypes.V2_APPLICATION_JSON_VALUE)
public class CidadeControllerV2 {

    private CidadeRepository cidadeRepository;
    private CadastroCidadeService cidadeCadastroService;
    private CidadeModelAssemblerV2 cidadeModelAssembler;
    private CidadeInputDisassemblerV2 cidadeInputDisassembler;

    @GetMapping
    public CollectionModel<CidadeModelV2> listar() {

        List<Cidade> todasCidades = cidadeRepository.findAll();
        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadeModelV2> buscar(@PathVariable Long id) {

        Cidade cidade = cidadeCadastroService.buscarOuFalhar(id);
        CidadeModelV2 cidadeModel = cidadeModelAssembler.toModel(cidade);

        return ResponseEntity.ok(cidadeModel);
    }

    @PostMapping
    public ResponseEntity<CidadeModelV2> adicionar(@RequestBody @Valid CidadeInputV2 novaCidade) {

        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(novaCidade);
            cidade = cidadeCadastroService.salvar(cidade);
            CidadeModelV2 cidadeModel = cidadeModelAssembler.toModel(cidade);

            URI uri = ResourceUriHelper.addUri(cidadeModel.getIdCidade());

            return ResponseEntity.created(uri).body(cidadeModel);

        } catch (EstadoNaoEncontradaException exception) {
            throw new NegocioException(exception.getMessage(), exception);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadeModelV2> atualizar(@PathVariable Long id,
                                                 @RequestBody @Valid CidadeInputV2 novaCidade) {

        try {
            Cidade cidadeAtual = cidadeCadastroService.buscarOuFalhar(id);

            cidadeInputDisassembler.copyToDomainObject(novaCidade, cidadeAtual);
            Cidade cidadeSalva = cidadeCadastroService.salvar(cidadeAtual);

            CidadeModelV2 cidadeModel = cidadeModelAssembler.toModel(cidadeSalva);

            return ResponseEntity.ok(cidadeModel);

        } catch (EstadoNaoEncontradaException exception) {
            throw new NegocioException(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {

        cidadeCadastroService.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
