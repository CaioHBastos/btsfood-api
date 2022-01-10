package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.ResourceUriHelper;
import br.com.btstech.btsfoodapi.api.v1.assembler.CidadeInputDisassembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.CidadeModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.model.CidadeModel;
import br.com.btstech.btsfoodapi.api.v1.model.input.CidadeInput;
import br.com.btstech.btsfoodapi.api.v1.openapi.controller.CidadeControllerOpenApi;
import br.com.btstech.btsfoodapi.domain.exception.EstadoNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.exception.NegocioException;
import br.com.btstech.btsfoodapi.domain.model.Cidade;
import br.com.btstech.btsfoodapi.domain.repository.CidadeRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroCidadeService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController implements CidadeControllerOpenApi {

    private CidadeRepository cidadeRepository;
    private CadastroCidadeService cidadeCadastroService;
    private CidadeModelAssembler cidadeModelAssembler;
    private CidadeInputDisassembler cidadeInputDisassembler;

    @Deprecated
    @Override
    @GetMapping
    public CollectionModel<CidadeModel> listar() {

        List<Cidade> todasCidades = cidadeRepository.findAll();
        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CidadeModel> buscar(@PathVariable Long id) {

        Cidade cidade = cidadeCadastroService.buscarOuFalhar(id);
        CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);

        return ResponseEntity.ok(cidadeModel);
    }

    @Override
    @PostMapping
    public ResponseEntity<CidadeModel> adicionar(@RequestBody @Valid CidadeInput novaCidade) {

        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(novaCidade);
            cidade = cidadeCadastroService.salvar(cidade);
            CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);

            URI uri = ResourceUriHelper.addUri(cidade.getId());

            return ResponseEntity.created(uri).body(cidadeModel);

        } catch (EstadoNaoEncontradaException exception) {
            throw new NegocioException(exception.getMessage(), exception);
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CidadeModel> atualizar(@PathVariable Long id,
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

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {

        cidadeCadastroService.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
