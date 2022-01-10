package br.com.btstech.btsfoodapi.api.v2.assembler;

import br.com.btstech.btsfoodapi.api.v2.BtsLinksV2;
import br.com.btstech.btsfoodapi.api.v2.controller.CozinhaControllerV2;
import br.com.btstech.btsfoodapi.api.v2.model.CozinhaModelV2;
import br.com.btstech.btsfoodapi.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CozinhaModelAssemblerV2
        extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModelV2> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BtsLinksV2 btsLinks;

    public CozinhaModelAssemblerV2() {
        super(CozinhaControllerV2.class, CozinhaModelV2.class);
    }

    @Override
    public CozinhaModelV2 toModel(Cozinha cozinha) {
        CozinhaModelV2 cozinhaModel = createModelWithId(cozinha.getId(), cozinha);
        modelMapper.map(cozinha, cozinhaModel);

        cozinhaModel.add(btsLinks.linkToCozinhas("cozinhas"));

        return cozinhaModel;
    }
}
