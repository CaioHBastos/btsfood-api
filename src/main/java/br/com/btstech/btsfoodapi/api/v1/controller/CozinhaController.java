package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.v1.assembler.CozinhaInputDisassembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.CozinhaModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.model.CozinhaModel;
import br.com.btstech.btsfoodapi.api.v1.model.input.CozinhaInput;
import br.com.btstech.btsfoodapi.api.v1.openapi.controller.CozinhaControllerOpenApi;
import br.com.btstech.btsfoodapi.core.security.CheckSecurity;
import br.com.btstech.btsfoodapi.domain.model.Cozinha;
import br.com.btstech.btsfoodapi.domain.repository.CozinhaRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroCozinhaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "/v1/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController implements CozinhaControllerOpenApi {

    private CozinhaRepository cozinhaRepository;
    private CadastroCozinhaService cadastroCozinhaService;
    private CozinhaModelAssembler cozinhaModelAssembler;
    private CozinhaInputDisassembler cozinhaInputDisassembler;
    private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

    @CheckSecurity.Cozinhas.PodeConsultar
    @Override
    @GetMapping
    public PagedModel<CozinhaModel> listar(Pageable pageable) {
        log.info("Consultando cozinhas...");

        Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);

        return pagedResourcesAssembler.toModel(cozinhasPage, cozinhaModelAssembler);
    }

    @CheckSecurity.Cozinhas.PodeConsultar
    @GetMapping("/{id}")
    public CozinhaModel buscar(@PathVariable Long id) {
        Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(id);

        return cozinhaModelAssembler.toModel(cozinha);
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @PostMapping
    public ResponseEntity<CozinhaModel> adicionar(@RequestBody @Valid CozinhaInput novaCozinha) {
        Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(novaCozinha);
        cozinha = cadastroCozinhaService.salvar(cozinha);

        CozinhaModel cozinhaModel = cozinhaModelAssembler.toModel(cozinha);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cozinhaModel);
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @PutMapping("/{id}")
    public CozinhaModel atualizar(@PathVariable Long id, @RequestBody @Valid CozinhaInput novaCozinha) {
        Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(id);
        cozinhaInputDisassembler.copyToDomainObject(novaCozinha, cozinhaAtual);
        cozinhaAtual = cadastroCozinhaService.salvar(cozinhaAtual);

        return cozinhaModelAssembler.toModel(cozinhaAtual);
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cadastroCozinhaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
