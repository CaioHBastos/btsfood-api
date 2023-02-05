package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.v1.assembler.EstadoInputDisassembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.EstadoModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.model.EstadoModel;
import br.com.btstech.btsfoodapi.api.v1.model.input.EstadoInput;
import br.com.btstech.btsfoodapi.api.v1.openapi.controller.EstadoControllerOpenApi;
import br.com.btstech.btsfoodapi.core.security.CheckSecurity;
import br.com.btstech.btsfoodapi.domain.model.Estado;
import br.com.btstech.btsfoodapi.domain.repository.EstadoRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroEstadoService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/v1/estados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadoController implements EstadoControllerOpenApi {

    private EstadoRepository estadoRepository;
    private CadastroEstadoService cadastroEstadoService;
    private EstadoModelAssembler estadoModelAssembler;
    private EstadoInputDisassembler estadoInputDisassembler;

    @CheckSecurity.Estados.PodeConsultar
    @GetMapping
    public CollectionModel<EstadoModel> listar() {
        List<Estado> todosEstados = estadoRepository.findAll();

        return estadoModelAssembler.toCollectionModel(todosEstados);
    }

    @CheckSecurity.Estados.PodeConsultar
    @GetMapping("/{id}")
    public ResponseEntity<EstadoModel> buscar(@PathVariable Long id) {
        Estado estado = cadastroEstadoService.buscarOuFalhar(id);
        EstadoModel estadoModel = estadoModelAssembler.toModel(estado);

        return ResponseEntity.ok(estadoModel);
    }

    @CheckSecurity.Estados.PodeEditar
    @PostMapping
    public ResponseEntity<EstadoModel> adicionar(@RequestBody @Valid EstadoInput novoEstado) {
        Estado estado = estadoInputDisassembler.toDomainObject(novoEstado);
        estado = cadastroEstadoService.salvar(estado);
        EstadoModel estadoModel = estadoModelAssembler.toModel(estado);

        return ResponseEntity.status(HttpStatus.CREATED).body(estadoModel);
    }

    @CheckSecurity.Estados.PodeEditar
    @PutMapping("/{id}")
    public ResponseEntity<EstadoModel> atualizar(@PathVariable Long id, @RequestBody @Valid EstadoInput novoEstado) {
        Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(id);
        estadoInputDisassembler.copyToDomainObject(novoEstado, estadoAtual);

        Estado estadoSalvo = cadastroEstadoService.salvar(estadoAtual);
        EstadoModel estadoModel = estadoModelAssembler.toModel(estadoSalvo);

        return ResponseEntity.ok(estadoModel);
    }

    @CheckSecurity.Estados.PodeEditar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cadastroEstadoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
