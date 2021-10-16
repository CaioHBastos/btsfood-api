package br.com.btstech.btsfoodapi.api.assembler;

import br.com.btstech.btsfoodapi.api.model.input.RestauranteInput;
import br.com.btstech.btsfoodapi.domain.model.Cidade;
import br.com.btstech.btsfoodapi.domain.model.Cozinha;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RestauranteInputDisassembler {

    private final ModelMapper modelMapper;

    public Restaurante toDomain(RestauranteInput restauranteInput) {
        return modelMapper.map(restauranteInput, Restaurante.class);
    }

    public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
        // Para evitar Caused by: org.hibernate.HibernateException: identifier of an instance of
        // br.com.btstech.btsfoodapi.domain.model.Cozinha was altered from 1 to 2
        restaurante.setCozinha(new Cozinha());

        if (restaurante.getEndereco() != null) {
            restaurante.getEndereco().setCidade(new Cidade());
        }

        modelMapper.map(restauranteInput, restaurante);
    }
}
