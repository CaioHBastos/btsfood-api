package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.EstadoInputDisassembler;
import br.com.btstech.btsfoodapi.api.assembler.EstadoModelAssembler;
import br.com.btstech.btsfoodapi.api.model.EstadoModel;
import br.com.btstech.btsfoodapi.api.model.input.EstadoInput;
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
    private EstadoModelAssembler estadoModelAssembler;
    private EstadoInputDisassembler estadoInputDisassembler;

    @GetMapping
    public List<EstadoModel> listar() {
        List<Estado> todosEstados = estadoRepository.findAll();

        return estadoModelAssembler.toCollectionModel(todosEstados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoModel> buscar(@PathVariable Long id) {
        Estado estado = cadastroEstadoService.buscarOuFalhar(id);
        EstadoModel estadoModel = estadoModelAssembler.toModel(estado);

        return ResponseEntity.ok(estadoModel);
    }

    @PostMapping
    public ResponseEntity<EstadoModel> adicionar(@RequestBody @Valid EstadoInput novoEstado) {
        Estado estado = estadoInputDisassembler.toDomainObject(novoEstado);
        estado = cadastroEstadoService.salvar(estado);
        EstadoModel estadoModel = estadoModelAssembler.toModel(estado);

        return ResponseEntity.status(HttpStatus.CREATED).body(estadoModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoModel> atualizar(@PathVariable Long id, @RequestBody @Valid EstadoInput novoEstado) {
        Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(id);
        estadoInputDisassembler.copyToDomainObject(novoEstado, estadoAtual);

        Estado estadoSalvo = cadastroEstadoService.salvar(estadoAtual);
        EstadoModel estadoModel = estadoModelAssembler.toModel(estadoSalvo);

        return ResponseEntity.ok(estadoModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cadastroEstadoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
