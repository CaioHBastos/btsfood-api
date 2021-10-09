package br.com.btstech.btsfoodapi.api.assembler;

import br.com.btstech.btsfoodapi.api.model.CozinhaModel;
import br.com.btstech.btsfoodapi.domain.model.Cozinha;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CozinhaModelAssembler {

    private final ModelMapper modelMapper;

    public CozinhaModel toModel(Cozinha cozinha) {
        return modelMapper.map(cozinha, CozinhaModel.class);
    }

    public List<CozinhaModel> toCollectionModel(List<Cozinha> cozinhas) {
        return cozinhas.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
