package br.com.btstech.btsfoodapi.api.assembler;

import br.com.btstech.btsfoodapi.api.model.CidadeModel;
import br.com.btstech.btsfoodapi.domain.model.Cidade;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CidadeModelAssembler {

    private final ModelMapper modelMapper;

    public CidadeModel toModel(Cidade cidade) {
        return modelMapper.map(cidade, CidadeModel.class);
    }

    public List<CidadeModel> toCollectionModel(List<Cidade> cidades) {
        return cidades.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
