package br.com.btstech.btsfoodapi.domain.event;

import br.com.btstech.btsfoodapi.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedidoCanceladoEvent {

    private Pedido pedido;
}
