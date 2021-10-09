package br.com.btstech.btsfoodapi.api.assembler;

import br.com.btstech.btsfoodapi.api.model.input.CozinhaInput;
import br.com.btstech.btsfoodapi.domain.model.Cozinha;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CozinhaInputDisassembler {

    private final ModelMapper modelMapper;

    public Cozinha toDomainObject(CozinhaInput cozinhaInput) {
        return modelMapper.map(cozinhaInput, Cozinha.class);
    }

    public void copyToDomainObject(CozinhaInput cozinhaInput, Cozinha cozinha) {
        modelMapper.map(cozinhaInput, cozinha);
    }

}
