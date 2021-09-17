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

@AllArgsConstructor
@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

    private CidadeRepository cidadeRepository;
    private CadastroCidadeService cidadeCadastroService;

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cidade> buscar(@PathVariable Long id) {
        Cidade cidade = cidadeRepository.buscar(id);

        if (cidade == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cidade);
    }

    @PostMapping
    public ResponseEntity<Cidade> adicionar(@RequestBody Cidade novaCidade) {
        Cidade cidade = cidadeCadastroService.salvar(novaCidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cidade novaCidade) {
        try {
            Cidade cidadeAtual = cidadeRepository.buscar(id);

            if (cidadeAtual == null) {
                return ResponseEntity.notFound().build();
            }

            BeanUtils.copyProperties(novaCidade, cidadeAtual, "id");

            cidadeCadastroService.salvar(cidadeAtual);
            return ResponseEntity.ok(cidadeAtual);

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
