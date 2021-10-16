package br.com.btstech.btsfoodapi.core.configuration;

import br.com.btstech.btsfoodapi.api.model.EnderecoModel;
import br.com.btstech.btsfoodapi.domain.model.Endereco;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();

        var enderecoToEnderecoModelTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);
        enderecoToEnderecoModelTypeMap.<String>addMapping(
                enderecosource -> enderecosource.getCidade().getEstado().getNome(),
                (enderecoDestination, enderecoValue) -> enderecoDestination.getCidade().setEstado(enderecoValue));

        return modelMapper;

        /*var modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
                .addMapping(Restaurante::getTaxaFrete, RestauranteModel::setPrecoFrete);*/
    }
}
