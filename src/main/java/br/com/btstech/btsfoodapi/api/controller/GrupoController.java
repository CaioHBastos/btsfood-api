package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.GrupoInputDisassembler;
import br.com.btstech.btsfoodapi.api.assembler.GrupoModelAssembler;
import br.com.btstech.btsfoodapi.api.openapi.controller.GrupoControllerOpenApi;
import br.com.btstech.btsfoodapi.api.model.GrupoModel;
import br.com.btstech.btsfoodapi.api.model.input.GrupoInput;
import br.com.btstech.btsfoodapi.domain.model.Grupo;
import br.com.btstech.btsfoodapi.domain.repository.GrupoRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroGrupoService;
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
@RequestMapping(path = "/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

    private GrupoRepository grupoRepository;
    private CadastroGrupoService cadastroGrupo;
    private GrupoModelAssembler grupoModelAssembler;
    private GrupoInputDisassembler grupoInputDisassembler;

    @GetMapping
    public CollectionModel<GrupoModel> listar() {
        List<Grupo> todosGrupos = grupoRepository.findAll();
        
        return grupoModelAssembler.toCollectionModel(todosGrupos);
    }
    
    @GetMapping("/{id}")
    public GrupoModel buscar(@PathVariable Long id) {
        Grupo grupo = cadastroGrupo.buscarOuFalhar(id);
        
        return grupoModelAssembler.toModel(grupo);
    }
    
    @PostMapping
    public ResponseEntity<GrupoModel> adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);
        
        grupo = cadastroGrupo.salvar(grupo);
        GrupoModel grupoModel = grupoModelAssembler.toModel(grupo);

        return ResponseEntity.status(HttpStatus.CREATED).body(grupoModel);
    }
    
    @PutMapping("/{id}")
    public GrupoModel atualizar(@PathVariable Long id, @RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupoAtual = cadastroGrupo.buscarOuFalhar(id);
        
        grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
        
        grupoAtual = cadastroGrupo.salvar(grupoAtual);
        
        return grupoModelAssembler.toModel(grupoAtual);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cadastroGrupo.excluir(id);
        return ResponseEntity.noContent().build();
    }   
}