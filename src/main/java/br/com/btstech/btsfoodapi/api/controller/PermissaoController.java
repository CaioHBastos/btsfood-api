package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.PermissaoModelAssembler;
import br.com.btstech.btsfoodapi.api.model.PermissaoModel;
import br.com.btstech.btsfoodapi.api.openapi.PermissaoControllerOpenApi;
import br.com.btstech.btsfoodapi.domain.model.Permissao;
import br.com.btstech.btsfoodapi.domain.repository.PermissaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissaoController implements PermissaoControllerOpenApi {

    private final PermissaoRepository permissaoRepository;
    private final PermissaoModelAssembler permissaoModelAssembler;

    @Override
    @GetMapping
    public CollectionModel<PermissaoModel> listar() {
        List<Permissao> todasPermissoes = permissaoRepository.findAll();

        return permissaoModelAssembler.toCollectionModel(todasPermissoes);
    }
}
