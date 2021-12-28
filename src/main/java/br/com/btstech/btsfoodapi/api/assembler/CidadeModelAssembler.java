package br.com.btstech.btsfoodapi.api.assembler;

import br.com.btstech.btsfoodapi.api.BtsLinks;
import br.com.btstech.btsfoodapi.api.controller.CidadeController;
import br.com.btstech.btsfoodapi.api.model.CidadeModel;
import br.com.btstech.btsfoodapi.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BtsLinks btsLinks;

    public CidadeModelAssembler() {
        super(CidadeController.class, CidadeModel.class);
    }

    @Override
    public CidadeModel toModel(Cidade cidade) {

        CidadeModel cidadeModel = createModelWithId(cidade.getId(), cidade);
        modelMapper.map(cidadeModel, cidadeModel);

        cidadeModel.add(btsLinks.linkToCidades("cidades"));

        cidadeModel.getEstado().add(btsLinks.linkToEstado(cidadeModel.getEstado().getId()));

        return cidadeModel;
    }

    @Override
    public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
        return super.toCollectionModel(entities)
                .add(btsLinks.linkToCidades());
    }
}
