package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.v1.assembler.UsuarioInputDisassembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.UsuarioModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.model.UsuarioModel;
import br.com.btstech.btsfoodapi.api.v1.model.input.SenhaInput;
import br.com.btstech.btsfoodapi.api.v1.model.input.UsuarioComSenhaInput;
import br.com.btstech.btsfoodapi.api.v1.model.input.UsuarioInput;
import br.com.btstech.btsfoodapi.api.v1.openapi.controller.UsuarioControllerOpenApi;
import br.com.btstech.btsfoodapi.core.security.CheckSecurity;
import br.com.btstech.btsfoodapi.domain.model.Usuario;
import br.com.btstech.btsfoodapi.domain.repository.UsuarioRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroUsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController implements UsuarioControllerOpenApi {

    private UsuarioRepository usuarioRepository;
    private CadastroUsuarioService cadastroUsuario;
    private UsuarioModelAssembler usuarioModelAssembler;
    private UsuarioInputDisassembler usuarioInputDisassembler;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<UsuarioModel> listar() {
        List<Usuario> todasUsuarios = usuarioRepository.findAll();

        return usuarioModelAssembler.toCollectionModel(todasUsuarios);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping("/{id}")
    public UsuarioModel buscar(@PathVariable Long id) {
        Usuario usuario = cadastroUsuario.buscarOuFalhar(id);

        return usuarioModelAssembler.toModel(usuario);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioInput) {
        Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);
        usuario = cadastroUsuario.salvar(usuario);

        return usuarioModelAssembler.toModel(usuario);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeAlterarUsuario
    @PutMapping("/{id}")
    public UsuarioModel atualizar(@PathVariable Long id,
            @RequestBody @Valid UsuarioInput usuarioInput) {
        Usuario usuarioAtual = cadastroUsuario.buscarOuFalhar(id);
        usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);
        usuarioAtual = cadastroUsuario.salvar(usuarioAtual);
        
        return usuarioModelAssembler.toModel(usuarioAtual);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeAlterarPropriaSenha
    @PutMapping("/{id}/senhas")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable Long id, @RequestBody @Valid SenhaInput senha) {
        cadastroUsuario.alterarSenha(id, senha.getSenhaAtual(), senha.getNovaSenha());
    }            
}