package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.model.input.EstadoInput;
import br.com.btstech.btsfoodapi.domain.model.Estado;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EstadoInputDisassembler {

    private final ModelMapper modelMapper;

    public Estado toDomainObject(EstadoInput estadoInput) {
        return modelMapper.map(estadoInput, Estado.class);
    }

    public void copyToDomainObject(EstadoInput estadoInput, Estado estado) {
        modelMapper.map(estadoInput, estado);
    }
}
