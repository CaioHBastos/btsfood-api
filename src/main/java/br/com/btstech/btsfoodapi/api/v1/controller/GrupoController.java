package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.v1.assembler.GrupoInputDisassembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.GrupoModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.openapi.controller.GrupoControllerOpenApi;
import br.com.btstech.btsfoodapi.api.v1.model.GrupoModel;
import br.com.btstech.btsfoodapi.api.v1.model.input.GrupoInput;
import br.com.btstech.btsfoodapi.core.security.CheckSecurity;
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
@RequestMapping(path = "/v1/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

    private GrupoRepository grupoRepository;
    private CadastroGrupoService cadastroGrupo;
    private GrupoModelAssembler grupoModelAssembler;
    private GrupoInputDisassembler grupoInputDisassembler;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<GrupoModel> listar() {
        List<Grupo> todosGrupos = grupoRepository.findAll();
        
        return grupoModelAssembler.toCollectionModel(todosGrupos);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping("/{id}")
    public GrupoModel buscar(@PathVariable Long id) {
        Grupo grupo = cadastroGrupo.buscarOuFalhar(id);
        
        return grupoModelAssembler.toModel(grupo);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PostMapping
    public ResponseEntity<GrupoModel> adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);
        
        grupo = cadastroGrupo.salvar(grupo);
        GrupoModel grupoModel = grupoModelAssembler.toModel(grupo);

        return ResponseEntity.status(HttpStatus.CREATED).body(grupoModel);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping("/{id}")
    public GrupoModel atualizar(@PathVariable Long id, @RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupoAtual = cadastroGrupo.buscarOuFalhar(id);
        
        grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);
        
        grupoAtual = cadastroGrupo.salvar(grupoAtual);
        
        return grupoModelAssembler.toModel(grupoAtual);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cadastroGrupo.excluir(id);
        return ResponseEntity.noContent().build();
    }   
}