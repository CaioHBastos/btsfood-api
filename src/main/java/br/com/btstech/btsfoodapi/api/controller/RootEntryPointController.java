package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.BtsLinks;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RootEntryPointController {

    BtsLinks btsLinks;

    @GetMapping
    public RootEntryPointModel root() {
        var rootEntryPointModel = new RootEntryPointModel();

        rootEntryPointModel.add(btsLinks.linkToCozinhas("cozinhas"));
        rootEntryPointModel.add(btsLinks.linkToPedidos("pedidos"));
        rootEntryPointModel.add(btsLinks.linkToRestaurantes("restaurantes"));
        rootEntryPointModel.add(btsLinks.linkToGrupos("grupos"));
        rootEntryPointModel.add(btsLinks.linkToPermissoes("permissoes"));
        rootEntryPointModel.add(btsLinks.linkToFormasPagamento("formas-pagamento"));
        rootEntryPointModel.add(btsLinks.linkToEstados("estados"));
        rootEntryPointModel.add(btsLinks.linkToCidades("cidades"));
        rootEntryPointModel.add(btsLinks.linkToEstatisticas("estatisticas"));

        return rootEntryPointModel;
    }

    private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel> {

    }
}
