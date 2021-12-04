package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.CozinhaInputDisassembler;
import br.com.btstech.btsfoodapi.api.assembler.CozinhaModelAssembler;
import br.com.btstech.btsfoodapi.api.model.CozinhaModel;
import br.com.btstech.btsfoodapi.api.model.input.CozinhaInput;
import br.com.btstech.btsfoodapi.api.openapi.controller.CozinhaControllerOpenApi;
import br.com.btstech.btsfoodapi.domain.model.Cozinha;
import br.com.btstech.btsfoodapi.domain.repository.CozinhaRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroCozinhaService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController implements CozinhaControllerOpenApi {

    private CozinhaRepository cozinhaRepository;
    private CadastroCozinhaService cadastroCozinhaService;
    private CozinhaModelAssembler cozinhaModelAssembler;
    private CozinhaInputDisassembler cozinhaInputDisassembler;

    @GetMapping
    public Page<CozinhaModel> listar(Pageable pageable) {
        Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
        List<CozinhaModel> cozinhaModels = cozinhaModelAssembler.toCollectionModel(cozinhasPage.getContent());

        return new PageImpl<>(cozinhaModels, pageable, cozinhasPage.getTotalElements());
    }

    @GetMapping("/{id}")
    public CozinhaModel buscar(@PathVariable Long id) {
        Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(id);

        return cozinhaModelAssembler.toModel(cozinha);
    }

    @PostMapping
    public ResponseEntity<CozinhaModel> adicionar(@RequestBody @Valid CozinhaInput novaCozinha) {
        Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(novaCozinha);
        cozinha = cadastroCozinhaService.salvar(cozinha);

        CozinhaModel cozinhaModel = cozinhaModelAssembler.toModel(cozinha);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cozinhaModel);
    }

    @PutMapping("/{id}")
    public CozinhaModel atualizar(@PathVariable Long id, @RequestBody @Valid CozinhaInput novaCozinha) {
        Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(id);
        cozinhaInputDisassembler.copyToDomainObject(novaCozinha, cozinhaAtual);
        cozinhaAtual = cadastroCozinhaService.salvar(cozinhaAtual);

        return cozinhaModelAssembler.toModel(cozinhaAtual);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cadastroCozinhaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
