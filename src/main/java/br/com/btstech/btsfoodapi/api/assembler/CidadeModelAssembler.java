package br.com.btstech.btsfoodapi.api.assembler;

import br.com.btstech.btsfoodapi.api.controller.CidadeController;
import br.com.btstech.btsfoodapi.api.controller.EstadoController;
import br.com.btstech.btsfoodapi.api.model.CidadeModel;
import br.com.btstech.btsfoodapi.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel> {

    @Autowired
    ModelMapper modelMapper;

    public CidadeModelAssembler() {
        super(CidadeController.class, CidadeModel.class);
    }

    @Override
    public CidadeModel toModel(Cidade cidade) {
        CidadeModel cidadeModel = modelMapper.map(cidade, CidadeModel.class);

        cidadeModel.add(linkTo(methodOn(CidadeController.class)
                .buscar(cidadeModel.getId()))
                .withSelfRel());

        cidadeModel.add(linkTo(methodOn(CidadeController.class)
                .listar())
                .withRel("cidades"));

        cidadeModel.getEstado().add(linkTo(methodOn(EstadoController.class)
                .buscar(cidadeModel.getEstado().getId()))
                .withSelfRel());

        return cidadeModel;
    }

    @Override
    public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(CidadeController.class).withSelfRel());
    }
}
