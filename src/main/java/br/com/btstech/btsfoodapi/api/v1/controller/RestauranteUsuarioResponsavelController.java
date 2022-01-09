package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.v1.BtsLinks;
import br.com.btstech.btsfoodapi.api.v1.assembler.UsuarioModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.model.UsuarioModel;
import br.com.btstech.btsfoodapi.api.v1.openapi.controller.RestauranteUsuarioResponsavelControllerOpenApi;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import br.com.btstech.btsfoodapi.domain.service.CadastroRestauranteService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/restaurantes/{restauranteId}/responsaveis",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteUsuarioResponsavelController implements RestauranteUsuarioResponsavelControllerOpenApi {

    private CadastroRestauranteService cadastroRestaurante;
    private UsuarioModelAssembler usuarioModelAssembler;
    private BtsLinks btsLinks;

    @GetMapping
    public CollectionModel<UsuarioModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        CollectionModel<UsuarioModel> usuariosModel = usuarioModelAssembler
                .toCollectionModel(restaurante.getResponsaveis())
                .removeLinks()
                .add(btsLinks.linkToRestauranteResponsaveis(restauranteId))
                .add(btsLinks.linkToRestauranteResponsavelAssociacao(restauranteId, "associar"));

        usuariosModel.getContent().stream().forEach(usuarioModel -> {
            usuarioModel.add(btsLinks.linkToRestauranteResponsavelDesassociacao(
                    restauranteId, usuarioModel.getId(), "desassociar"));
        });

        return usuariosModel;
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        cadastroRestaurante.desassociarResponsavel(restauranteId, usuarioId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
        cadastroRestaurante.associarResponsavel(restauranteId, usuarioId);

        return ResponseEntity.noContent().build();
    }
}