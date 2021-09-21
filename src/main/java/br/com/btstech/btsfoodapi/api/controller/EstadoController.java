package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.domain.exception.EntidadeEmUsoException;
import br.com.btstech.btsfoodapi.domain.exception.EntidadeNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.model.Estado;
import br.com.btstech.btsfoodapi.domain.repository.EstadoRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroEstadoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/estados")
public class EstadoController {

    private EstadoRepository estadoRepository;
    private CadastroEstadoService cadastroEstadoService;

    @GetMapping
    public List<Estado> listar() {
        return estadoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estado> buscar(@PathVariable Long id) {
        Optional<Estado> estado = estadoRepository.findById(id);

        if (estado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(estado.get());
    }

    @PostMapping
    public ResponseEntity<Estado> adicionar(@RequestBody Estado novoEstado) {
        Estado estado = cadastroEstadoService.salvar(novoEstado);
        return ResponseEntity.status(HttpStatus.CREATED).body(estado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estado> atualizar(@PathVariable Long id, @RequestBody Estado novoEstado) {
        Optional<Estado> estadoAtual = estadoRepository.findById(id);

        if (estadoAtual.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BeanUtils.copyProperties(novoEstado, estadoAtual.get(), "id");

        Estado estadoSalvo = cadastroEstadoService.salvar(estadoAtual.get());
        return ResponseEntity.ok(estadoSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        try {
            cadastroEstadoService.excluir(id);
            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException exception) {
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
