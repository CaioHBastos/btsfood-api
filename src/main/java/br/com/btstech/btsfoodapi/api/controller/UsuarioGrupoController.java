package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.GrupoModelAssembler;
import br.com.btstech.btsfoodapi.api.model.GrupoModel;
import br.com.btstech.btsfoodapi.api.openapi.controller.UsuarioGrupoControllerOpenApi;
import br.com.btstech.btsfoodapi.domain.model.Usuario;
import br.com.btstech.btsfoodapi.domain.service.CadastroUsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/usuarios/{usuarioId}/grupos",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioGrupoController implements UsuarioGrupoControllerOpenApi {

    private CadastroUsuarioService cadastroUsuario;
    private GrupoModelAssembler grupoModelAssembler;

    @GetMapping
    public ResponseEntity<List<GrupoModel>> listar(@PathVariable Long usuarioId) {
        Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
        List<GrupoModel> grupoModels = grupoModelAssembler.toCollectionModel(usuario.getGrupos());

        return ResponseEntity.ok(grupoModels);
    }

    @DeleteMapping("/{grupoId}")
    public ResponseEntity<Void> desassociar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
        cadastroUsuario.desassociarGrupo(usuarioId, grupoId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{grupoId}")
    public ResponseEntity<Void> associar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
        cadastroUsuario.associarGrupo(usuarioId, grupoId);

        return ResponseEntity.noContent().build();
    }
}
