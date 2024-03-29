package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.v1.BtsLinks;
import br.com.btstech.btsfoodapi.api.v1.assembler.ProdutoInputDisassembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.ProdutoModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.model.ProdutoModel;
import br.com.btstech.btsfoodapi.api.v1.model.input.ProdutoInput;
import br.com.btstech.btsfoodapi.api.v1.openapi.controller.RestauranteProdutoControllerOpenApi;
import br.com.btstech.btsfoodapi.core.security.CheckSecurity;
import br.com.btstech.btsfoodapi.domain.model.Produto;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import br.com.btstech.btsfoodapi.domain.repository.ProdutoRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroProdutoService;
import br.com.btstech.btsfoodapi.domain.service.CadastroRestauranteService;
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
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/produtos",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {

    private ProdutoRepository produtoRepository;
    private CadastroProdutoService cadastroProduto;
    private CadastroRestauranteService cadastroRestaurante;
    private ProdutoModelAssembler produtoModelAssembler;
    private ProdutoInputDisassembler produtoInputDisassembler;
    private BtsLinks btsLinks;

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping
    public ResponseEntity<CollectionModel<ProdutoModel>> listar(@PathVariable Long restauranteId,
                                                                @RequestParam(required = false) Boolean incluirInativos) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        List<Produto> todosProdutos;

        if (incluirInativos) {
            todosProdutos = produtoRepository.findTodosByRestaurante(restaurante);
        } else {
            todosProdutos = produtoRepository.findAtivosByRestaurante(restaurante);
        }

        CollectionModel<ProdutoModel> produtoModels = produtoModelAssembler.toCollectionModel(todosProdutos)
                .add(btsLinks.linkToProdutos(restauranteId));

        return ResponseEntity.ok(produtoModels);
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping("/{produtoId}")
    public ResponseEntity<ProdutoModel> buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);
        ProdutoModel produtoModel = produtoModelAssembler.toModel(produto);

        return ResponseEntity.ok(produtoModel);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PostMapping
    public ResponseEntity<ProdutoModel> adicionar(@PathVariable Long restauranteId,
                                  @RequestBody @Valid ProdutoInput produtoInput) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
        produto.setRestaurante(restaurante);

        produto = cadastroProduto.salvar(produto);
        ProdutoModel produtoModel = produtoModelAssembler.toModel(produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(produtoModel);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PutMapping("/{produtoId}")
    public ResponseEntity<ProdutoModel> atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                                  @RequestBody @Valid ProdutoInput produtoInput) {
        Produto produtoAtual = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);

        produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);

        produtoAtual = cadastroProduto.salvar(produtoAtual);
        ProdutoModel produtoModel = produtoModelAssembler.toModel(produtoAtual);

        return ResponseEntity.ok(produtoModel);
    }
}
