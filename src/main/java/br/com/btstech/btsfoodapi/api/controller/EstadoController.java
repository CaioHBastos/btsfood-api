package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.domain.model.Estado;
import br.com.btstech.btsfoodapi.domain.repository.EstadoRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroEstadoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
        Estado estado = cadastroEstadoService.buscarOuFalhar(id);

        return ResponseEntity.ok(estado);
    }

    @PostMapping
    public ResponseEntity<Estado> adicionar(@RequestBody @Valid Estado novoEstado) {
        Estado estado = cadastroEstadoService.salvar(novoEstado);
        return ResponseEntity.status(HttpStatus.CREATED).body(estado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estado> atualizar(@PathVariable Long id, @RequestBody @Valid Estado novoEstado) {
        Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(id);

        BeanUtils.copyProperties(novoEstado, estadoAtual, "id");

        Estado estadoSalvo = cadastroEstadoService.salvar(estadoAtual);
        return ResponseEntity.ok(estadoSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cadastroEstadoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
