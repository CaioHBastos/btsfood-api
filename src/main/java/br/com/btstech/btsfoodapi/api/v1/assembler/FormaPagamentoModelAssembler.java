package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.BtsLinks;
import br.com.btstech.btsfoodapi.api.v1.controller.FormaPagamentoController;
import br.com.btstech.btsfoodapi.api.v1.model.FormaPagamentoModel;
import br.com.btstech.btsfoodapi.domain.model.FormaPagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class FormaPagamentoModelAssembler extends RepresentationModelAssemblerSupport<FormaPagamento, FormaPagamentoModel> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BtsLinks btsLinks;

    public FormaPagamentoModelAssembler() {
        super(FormaPagamentoController.class, FormaPagamentoModel.class);
    }

    @Override
    public FormaPagamentoModel toModel(FormaPagamento formaPagamento) {
        FormaPagamentoModel formaPagamentoModel =
                createModelWithId(formaPagamento.getId(), formaPagamento);

        modelMapper.map(formaPagamento, formaPagamentoModel);

        formaPagamentoModel.add(btsLinks.linkToFormasPagamento("formasPagamento"));

        return formaPagamentoModel;
    }

    @Override
    public CollectionModel<FormaPagamentoModel> toCollectionModel(Iterable<? extends FormaPagamento> entities) {
        return super.toCollectionModel(entities)
                .add(btsLinks.linkToFormasPagamento());
    }
}
