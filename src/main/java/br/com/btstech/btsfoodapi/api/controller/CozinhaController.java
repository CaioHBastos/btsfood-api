package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.domain.model.Cozinha;
import br.com.btstech.btsfoodapi.domain.repository.CozinhaRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroCozinhaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
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

    @GetMapping
    public List<Cozinha> listar() {
        return cozinhaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cozinha buscar(@PathVariable Long id) {
        return cadastroCozinhaService.buscarOuFalhar(id);
    }

    @PostMapping
    public ResponseEntity<Cozinha> adicionar(@RequestBody @Valid Cozinha novaCozinha) {
        Cozinha cozinha = cadastroCozinhaService.salvar(novaCozinha);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cozinha);
    }

    @PutMapping("/{id}")
    public Cozinha atualizar(@PathVariable Long id, @RequestBody @Valid Cozinha novaCozinha) {
        Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(id);

        BeanUtils.copyProperties(novaCozinha, cozinhaAtual, "id");

        return cadastroCozinhaService.salvar(cozinhaAtual);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cadastroCozinhaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
