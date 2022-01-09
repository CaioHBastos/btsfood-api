package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.BtsLinks;
import br.com.btstech.btsfoodapi.api.v1.controller.RestauranteController;
import br.com.btstech.btsfoodapi.api.v1.model.RestauranteModel;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BtsLinks btsLinks;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        modelMapper.map(restaurante, restauranteModel);

        restauranteModel.add(btsLinks.linkToRestaurantes("restaurantes"));

        if (restaurante.ativacaoPermitida()) {
            restauranteModel.add(
                    btsLinks.linkToRestauranteAtivacao(restaurante.getId(), "ativar"));
        }

        if (restaurante.inativacaoPermitida()) {
            restauranteModel.add(
                    btsLinks.linkToRestauranteInativacao(restaurante.getId(), "inativar"));
        }

        if (restaurante.aberturaPermitida()) {
            restauranteModel.add(
                    btsLinks.linkToRestauranteAbertura(restaurante.getId(), "abrir"));
        }

        if (restaurante.fechamentoPermitido()) {
            restauranteModel.add(
                    btsLinks.linkToRestauranteFechamento(restaurante.getId(), "fechar"));
        }

        restauranteModel.add(btsLinks.linkToProdutos(restaurante.getId(), "produtos"));

        restauranteModel.getCozinha().add(
                btsLinks.linkToCozinha(restaurante.getCozinha().getId()));

        if (restauranteModel.getEndereco() != null
                && restauranteModel.getEndereco().getCidade() != null) {
            restauranteModel.getEndereco().getCidade().add(
                    btsLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
        }

        restauranteModel.add(btsLinks.linkToRestauranteFormasPagamento(restaurante.getId(),
                "formas-pagamento"));

        restauranteModel.add(btsLinks.linkToRestauranteResponsaveis(restaurante.getId(),
                "responsaveis"));

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        return super.toCollectionModel(entities)
                .add(btsLinks.linkToRestaurantes());
    }
}
