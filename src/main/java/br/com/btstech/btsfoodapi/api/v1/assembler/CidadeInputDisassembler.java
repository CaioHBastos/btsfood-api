package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.model.input.CidadeInput;
import br.com.btstech.btsfoodapi.domain.model.Cidade;
import br.com.btstech.btsfoodapi.domain.model.Estado;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CidadeInputDisassembler {

    private final ModelMapper modelMapper;

    public Cidade toDomainObject(CidadeInput cidadeInput) {
        return modelMapper.map(cidadeInput, Cidade.class);
    }

    public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade) {
        // Para evitar org.hibernate.HibernateException: identifier of an instance of
        // com.algaworks.algafood.domain.model.Estado was altered from 1 to 2
        cidade.setEstado(new Estado());

        modelMapper.map(cidadeInput, cidade);
    }

}
