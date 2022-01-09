package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.model.input.PedidoInput;
import br.com.btstech.btsfoodapi.domain.model.Pedido;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PedidoInputDisassembler {

    private ModelMapper modelMapper;

    public Pedido toDomainObject(PedidoInput pedidoInput) {
        return modelMapper.map(pedidoInput, Pedido.class);
    }

    public void copyToDomainObject(PedidoInput pedidoInput, Pedido pedido) {
        modelMapper.map(pedidoInput, pedido);
    }
}
