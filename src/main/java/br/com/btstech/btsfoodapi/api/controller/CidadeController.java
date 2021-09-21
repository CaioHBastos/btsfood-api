package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.domain.exception.EntidadeEmUsoException;
import br.com.btstech.btsfoodapi.domain.exception.EntidadeNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.model.Cidade;
import br.com.btstech.btsfoodapi.domain.repository.CidadeRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroCidadeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

    private CidadeRepository cidadeRepository;
    private CadastroCidadeService cidadeCadastroService;

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cidade> buscar(@PathVariable Long id) {
        Optional<Cidade> cidade = cidadeRepository.findById(id);

        if (cidade.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cidade.get());
    }

    @PostMapping
    public ResponseEntity<Cidade> adicionar(@RequestBody Cidade novaCidade) {
        Cidade cidade = cidadeCadastroService.salvar(novaCidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cidade novaCidade) {
        try {
            Optional<Cidade> cidadeAtual = cidadeRepository.findById(id);

            if (cidadeAtual.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            BeanUtils.copyProperties(novaCidade, cidadeAtual.get(), "id");

            Cidade cidadeSalva = cidadeCadastroService.salvar(cidadeAtual.get());
            return ResponseEntity.ok(cidadeSalva);

        } catch (EntidadeNaoEncontradaException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            cidadeCadastroService.excluir(id);
            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException exception) {
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
