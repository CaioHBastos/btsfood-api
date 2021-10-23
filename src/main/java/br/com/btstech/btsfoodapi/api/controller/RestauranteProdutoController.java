package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.ProdutoInputDisassembler;
import br.com.btstech.btsfoodapi.api.assembler.ProdutoModelAssembler;
import br.com.btstech.btsfoodapi.api.model.ProdutoModel;
import br.com.btstech.btsfoodapi.api.model.input.ProdutoInput;
import br.com.btstech.btsfoodapi.domain.model.Produto;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import br.com.btstech.btsfoodapi.domain.repository.ProdutoRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroProdutoService;
import br.com.btstech.btsfoodapi.domain.service.CadastroRestauranteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {

    private ProdutoRepository produtoRepository;
    private CadastroProdutoService cadastroProduto;
    private CadastroRestauranteService cadastroRestaurante;
    private ProdutoModelAssembler produtoModelAssembler;
    private ProdutoInputDisassembler produtoInputDisassembler;

    @GetMapping
    public ResponseEntity<List<ProdutoModel>> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        List<Produto> todosProdutos = produtoRepository.findByRestaurante(restaurante);
        List<ProdutoModel> produtoModels = produtoModelAssembler.toCollectionModel(todosProdutos);

        return ResponseEntity.ok(produtoModels);
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<ProdutoModel> buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);
        ProdutoModel produtoModel = produtoModelAssembler.toModel(produto);

        return ResponseEntity.ok(produtoModel);
    }

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