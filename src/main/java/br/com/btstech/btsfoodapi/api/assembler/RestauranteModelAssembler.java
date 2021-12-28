package br.com.btstech.btsfoodapi.api.assembler;

import br.com.btstech.btsfoodapi.api.BtsLinks;
import br.com.btstech.btsfoodapi.api.controller.RestauranteController;
import br.com.btstech.btsfoodapi.api.model.CozinhaModel;
import br.com.btstech.btsfoodapi.api.model.RestauranteModel;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private BtsLinks btsLinks;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    @Override
    public RestauranteModel toModel(Restaurante restaurante) {
        RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        modelMapper.map(restaurante, restauranteModel);

        restauranteModel.add(btsLinks.linkToRestaurantes("restaurantes"));

        restauranteModel.getCozinha().add(
                btsLinks.linkToCozinha(restaurante.getCozinha().getId()));

        restauranteModel.getEndereco().getCidade().add(
                btsLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));

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
