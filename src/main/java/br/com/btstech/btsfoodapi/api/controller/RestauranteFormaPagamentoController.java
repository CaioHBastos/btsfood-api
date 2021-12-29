package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.BtsLinks;
import br.com.btstech.btsfoodapi.api.assembler.FormaPagamentoModelAssembler;
import br.com.btstech.btsfoodapi.api.model.FormaPagamentoModel;
import br.com.btstech.btsfoodapi.api.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import br.com.btstech.btsfoodapi.domain.service.CadastroRestauranteService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/restaurantes/{restauranteId}/formas-pagamento",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteFormaPagamentoController implements RestauranteFormaPagamentoControllerOpenApi {

    private CadastroRestauranteService cadastroRestauranteService;
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
    private BtsLinks btsLinks;

    @GetMapping
    public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(@PathVariable("id") Long restauranteId) {
        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);

        CollectionModel<FormaPagamentoModel> formasPagamento =
                formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento())
                .removeLinks()
                .add(btsLinks.linkToRestauranteFormasPagamento(restauranteId));

        return ResponseEntity.ok(formasPagamento);
    }

    @PutMapping("/{formaPagamentoId}")
    public ResponseEntity<Void> associar(@PathVariable("id") Long restauranteId, @PathVariable Long formaPagamentoId) {
        cadastroRestauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{formaPagamentoId}")
    public ResponseEntity<Void> desassociar(@PathVariable("id") Long restauranteId, @PathVariable Long formaPagamentoId) {
        cadastroRestauranteService.desassociarFormaPagamento(restauranteId, formaPagamentoId);

        return ResponseEntity.noContent().build();
    }
}

