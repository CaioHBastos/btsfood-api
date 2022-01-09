package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.BtsLinks;
import br.com.btstech.btsfoodapi.api.v1.controller.PedidoController;
import br.com.btstech.btsfoodapi.api.v1.model.PedidoResumoModel;
import br.com.btstech.btsfoodapi.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoResumoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoModel> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BtsLinks btsLinks;

    public PedidoResumoModelAssembler() {
        super(PedidoController.class, PedidoResumoModel.class);
    }

    @Override
    public PedidoResumoModel toModel(Pedido pedido) {
        PedidoResumoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);

        pedidoModel.add(btsLinks.linkToPedidos("pedidos"));

        pedidoModel.getRestaurante().add(
                btsLinks.linkToRestaurante(pedido.getRestaurante().getId()));

        pedidoModel.getCliente().add(btsLinks.linkToUsuario(pedido.getCliente().getId()));

        return pedidoModel;
    }

    public List<PedidoResumoModel> toCollectionModel(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

}
