package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.PermissaoModelAssembler;
import br.com.btstech.btsfoodapi.api.model.PermissaoModel;
import br.com.btstech.btsfoodapi.api.openapi.controller.GrupoPermissaoControllerOpenApi;
import br.com.btstech.btsfoodapi.domain.model.Grupo;
import br.com.btstech.btsfoodapi.domain.service.CadastroGrupoService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/grupos/{grupoId}/permissoes",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {

    private CadastroGrupoService cadastroGrupo;
    private PermissaoModelAssembler permissaoModelAssembler;

    @GetMapping
    public ResponseEntity<List<PermissaoModel>> listar(@PathVariable Long grupoId) {
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
        List<PermissaoModel> permissaoModels = permissaoModelAssembler.toCollectionModel(grupo.getPermissoes());

        return ResponseEntity.ok(permissaoModels);
    }

    @DeleteMapping("/{permissaoId}")
    public ResponseEntity<Void> desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        cadastroGrupo.desassociarPermissao(grupoId, permissaoId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{permissaoId}")
    public ResponseEntity<Void> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
        cadastroGrupo.associarPermissao(grupoId, permissaoId);

        return ResponseEntity.noContent().build();
    }

}
