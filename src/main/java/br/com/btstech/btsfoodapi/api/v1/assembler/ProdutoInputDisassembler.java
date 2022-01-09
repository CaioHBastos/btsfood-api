package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.model.input.ProdutoInput;
import br.com.btstech.btsfoodapi.domain.model.Produto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ProdutoInputDisassembler {

    private ModelMapper modelMapper;

    public Produto toDomainObject(ProdutoInput produtoInput) {
        return modelMapper.map(produtoInput, Produto.class);
    }

    public void copyToDomainObject(ProdutoInput produtoInput, Produto produto) {
        modelMapper.map(produtoInput, produto);
    }
}
