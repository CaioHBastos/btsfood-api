package br.com.btstech.btsfoodapi.api.assembler;

import br.com.btstech.btsfoodapi.api.BtsLinks;
import br.com.btstech.btsfoodapi.api.controller.PedidoController;
import br.com.btstech.btsfoodapi.api.model.PedidoModel;
import br.com.btstech.btsfoodapi.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BtsLinks btsLinks;

    public PedidoModelAssembler() {
        super(PedidoController.class, PedidoModel.class);
    }

    @Override
    public PedidoModel toModel(Pedido pedido) {
        PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
        modelMapper.map(pedido, pedidoModel);

        pedidoModel.add(btsLinks.linkToPedidos("pedidos"));

        if (pedido.podeSerConfirmado()) {
            pedidoModel.add(btsLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
        }

        if (pedido.poderSerCancelado()) {
            pedidoModel.add(btsLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
        }

        if (pedido.poderSerEntregue()) {
            pedidoModel.add(btsLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
        }

        pedidoModel.getRestaurante().add(
                btsLinks.linkToRestaurante(pedido.getRestaurante().getId()));

        pedidoModel.getCliente().add(
                btsLinks.linkToUsuario(pedido.getCliente().getId()));

        pedidoModel.getFormaPagamento().add(
                btsLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));

        pedidoModel.getEnderecoEntrega().getCidade().add(
                btsLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));

        pedidoModel.getItens().forEach(item -> {
            item.add(btsLinks.linkToProduto(
                    pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto"));
        });

        return pedidoModel;
    }

    public List<PedidoModel> toCollectionModel(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(pedido -> toModel(pedido))
                .collect(Collectors.toList());
    }

}
