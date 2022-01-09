package br.com.btstech.btsfoodapi.api.v2.controller;

import br.com.btstech.btsfoodapi.api.ResourceUriHelper;
import br.com.btstech.btsfoodapi.api.v2.assembler.CidadeInputDisassemblerV2;
import br.com.btstech.btsfoodapi.api.v2.assembler.CidadeModelAssemblerV2;
import br.com.btstech.btsfoodapi.api.v2.model.CidadeModelV2;
import br.com.btstech.btsfoodapi.api.v2.model.input.CidadeInputV2;
import br.com.btstech.btsfoodapi.domain.exception.EstadoNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.exception.NegocioException;
import br.com.btstech.btsfoodapi.domain.model.Cidade;
import br.com.btstech.btsfoodapi.domain.repository.CidadeRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroCidadeService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "v2/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeControllerV2 {

    private CidadeRepository cidadeRepository;
    private CadastroCidadeService cadastroCidade;
    private CidadeModelAssemblerV2 cidadeModelAssembler;
    private CidadeInputDisassemblerV2 cidadeInputDisassembler;

    @GetMapping
    public CollectionModel<CidadeModelV2> listar() {
        List<Cidade> todasCidades = cidadeRepository.findAll();

        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }

    @GetMapping(value = "/{cidadeId}")
    public CidadeModelV2 buscar(@PathVariable Long cidadeId) {
        Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);

        return cidadeModelAssembler.toModel(cidade);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModelV2 adicionar(@RequestBody @Valid CidadeInputV2 cidadeInput) {
        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

            cidade = cadastroCidade.salvar(cidade);

            CidadeModelV2 cidadeModel = cidadeModelAssembler.toModel(cidade);

            ResourceUriHelper.addUri(cidadeModel.getIdCidade());

            return cidadeModel;
        } catch (EstadoNaoEncontradaException exception) {
            throw new NegocioException(exception.getMessage(), exception);
        }
    }

    @PutMapping(value = "/{cidadeId}")
    public CidadeModelV2 atualizar(@PathVariable Long cidadeId,
                                   @RequestBody @Valid CidadeInputV2 cidadeInput) {
        try {
            Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);

            cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

            cidadeAtual = cadastroCidade.salvar(cidadeAtual);

            return cidadeModelAssembler.toModel(cidadeAtual);
        } catch (EstadoNaoEncontradaException exception) {
            throw new NegocioException(exception.getMessage(), exception);
        }
    }

//  Não pode ser mapeado na mesma URL em um MediaType diferente, já que não aceita entrada e retorna void.
//	@DeleteMapping(value = "/{cidadeId}", produces = {})
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void remover(@PathVariable Long cidadeId) {
//		cadastroCidade.excluir(cidadeId);
//	}
}
