package br.com.btstech.btsfoodapi.api.assembler;

import br.com.btstech.btsfoodapi.api.model.CozinhaModel;
import br.com.btstech.btsfoodapi.api.model.RestauranteModel;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RestauranteModelAssembler {

    private final ModelMapper modelMapper;

    public RestauranteModel toModel(Restaurante restaurante) {
        return modelMapper.map(restaurante, RestauranteModel.class);
    }

    public List<RestauranteModel> toCollectionModel(List<Restaurante> restaurantes) {
        return restaurantes.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
