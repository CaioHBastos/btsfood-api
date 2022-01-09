package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.model.input.GrupoInput;
import br.com.btstech.btsfoodapi.domain.model.Grupo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GrupoInputDisassembler {

    private ModelMapper modelMapper;
    
    public Grupo toDomainObject(GrupoInput grupoInput) {
        return modelMapper.map(grupoInput, Grupo.class);
    }
    
    public void copyToDomainObject(GrupoInput grupoInput, Grupo grupo) {
        modelMapper.map(grupoInput, grupo);
    }   
} 