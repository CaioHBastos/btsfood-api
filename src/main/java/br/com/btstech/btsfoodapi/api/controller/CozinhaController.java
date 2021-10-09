package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.CozinhaInputDisassembler;
import br.com.btstech.btsfoodapi.api.assembler.CozinhaModelAssembler;
import br.com.btstech.btsfoodapi.api.model.CozinhaModel;
import br.com.btstech.btsfoodapi.api.model.input.CozinhaInput;
import br.com.btstech.btsfoodapi.domain.model.Cozinha;
import br.com.btstech.btsfoodapi.domain.repository.CozinhaRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroCozinhaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaController {

    private CozinhaRepository cozinhaRepository;
    private CadastroCozinhaService cadastroCozinhaService;
    private CozinhaModelAssembler cozinhaModelAssembler;
    private CozinhaInputDisassembler cozinhaInputDisassembler;

    @GetMapping
    public List<CozinhaModel> listar() {
        List<Cozinha> todasCozinhas = cozinhaRepository.findAll();

        return cozinhaModelAssembler.toCollectionModel(todasCozinhas);
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
