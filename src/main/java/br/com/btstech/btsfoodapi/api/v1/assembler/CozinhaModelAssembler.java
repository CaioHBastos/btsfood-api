package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.BtsLinks;
import br.com.btstech.btsfoodapi.api.v1.controller.CozinhaController;
import br.com.btstech.btsfoodapi.api.v1.model.CozinhaModel;
import br.com.btstech.btsfoodapi.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CozinhaModelAssembler extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BtsLinks btsLinks;

    public CozinhaModelAssembler() {
        super(CozinhaController.class, CozinhaModel.class);
    }

    @Override
    public CozinhaModel toModel(Cozinha cozinha) {
        CozinhaModel cozinhaModel = createModelWithId(cozinha.getId(), cozinha);
        modelMapper.map(cozinha, cozinhaModel);

        cozinhaModel.add(btsLinks.linkToCozinhas("cozinhas"));

        return cozinhaModel;
    }
}
