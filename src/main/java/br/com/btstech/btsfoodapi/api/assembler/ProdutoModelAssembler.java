package br.com.btstech.btsfoodapi.api.assembler;

import br.com.btstech.btsfoodapi.api.BtsLinks;
import br.com.btstech.btsfoodapi.api.controller.RestauranteProdutoController;
import br.com.btstech.btsfoodapi.api.model.ProdutoModel;
import br.com.btstech.btsfoodapi.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ProdutoModelAssembler extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BtsLinks btsLinks;

    public ProdutoModelAssembler() {
        super(RestauranteProdutoController.class, ProdutoModel.class);
    }

    @Override
    public ProdutoModel toModel(Produto produto) {
        ProdutoModel produtoModel = createModelWithId(
                produto.getId(), produto, produto.getRestaurante().getId());

        modelMapper.map(produto, produtoModel);

        produtoModel.add(btsLinks.linkToProdutos(produto.getRestaurante().getId(), "produtos"));

        produtoModel.add(btsLinks.linkToFotoProduto(
                produto.getRestaurante().getId(), produto.getId(), "foto"));

        return produtoModel;
    }
}