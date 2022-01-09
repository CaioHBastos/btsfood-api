package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.model.input.UsuarioInput;
import br.com.btstech.btsfoodapi.domain.model.Usuario;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UsuarioInputDisassembler {

    private ModelMapper modelMapper;
    
    public Usuario toDomainObject(UsuarioInput usuarioInput) {
        return modelMapper.map(usuarioInput, Usuario.class);
    }
    
    public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
        modelMapper.map(usuarioInput, usuario);
    }            
}