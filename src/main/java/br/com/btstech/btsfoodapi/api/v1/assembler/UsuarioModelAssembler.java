package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.BtsLinks;
import br.com.btstech.btsfoodapi.api.v1.controller.UsuarioController;
import br.com.btstech.btsfoodapi.api.v1.model.UsuarioModel;
import br.com.btstech.btsfoodapi.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UsuarioModelAssembler extends RepresentationModelAssemblerSupport<Usuario, UsuarioModel> {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BtsLinks btsLinks;

    public UsuarioModelAssembler() {
        super(UsuarioController.class, UsuarioModel.class);
    }

    @Override
    public UsuarioModel toModel(Usuario usuario) {
        UsuarioModel usuarioModel = createModelWithId(usuario.getId(), usuario);
        modelMapper.map(usuario, usuarioModel);

        usuarioModel.add(btsLinks.linkToUsuarios("usuarios"));
        usuarioModel.add(btsLinks.linkToGruposUsuario(usuario.getId(), "grupos-usuario"));

        return usuarioModel;
    }

    @Override
    public CollectionModel<UsuarioModel> toCollectionModel(Iterable<? extends Usuario> entities) {
        return super.toCollectionModel(entities)
                .add(btsLinks.linkToUsuarios());
    }
}