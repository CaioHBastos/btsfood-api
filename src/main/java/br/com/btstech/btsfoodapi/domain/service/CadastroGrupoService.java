package br.com.btstech.btsfoodapi.domain.service;

import br.com.btstech.btsfoodapi.domain.exception.EntidadeEmUsoException;
import br.com.btstech.btsfoodapi.domain.exception.GrupoNaoEncontradoException;
import br.com.btstech.btsfoodapi.domain.model.Grupo;
import br.com.btstech.btsfoodapi.domain.model.Permissao;
import br.com.btstech.btsfoodapi.domain.repository.GrupoRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CadastroGrupoService {

    private static final String MSG_GRUPO_EM_USO 
        = "Grupo de código %d não pode ser removido, pois está em uso";
    
    private GrupoRepository grupoRepository;
    private CadastroPermissaoService cadastroPermissao;

    @Transactional
    public Grupo salvar(Grupo grupo) {
        return grupoRepository.save(grupo);
    }
    
    @Transactional
    public void excluir(Long grupoId) {
        try {
            grupoRepository.deleteById(grupoId);
            grupoRepository.flush();
            
        } catch (EmptyResultDataAccessException e) {
            throw new GrupoNaoEncontradoException(grupoId);
        
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format(MSG_GRUPO_EM_USO, grupoId));
        }
    }

    public Grupo buscarOuFalhar(Long grupoId) {
        return grupoRepository.findById(grupoId)
            .orElseThrow(() -> new GrupoNaoEncontradoException(grupoId));
    }

    @Transactional
    public void desassociarPermissao(Long grupoId, Long permissaoId) {
        Grupo grupo = buscarOuFalhar(grupoId);
        Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);

        grupo.removerPermissao(permissao);
    }

    @Transactional
    public void associarPermissao(Long grupoId, Long permissaoId) {
        Grupo grupo = buscarOuFalhar(grupoId);
        Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);

        grupo.adicionarPermissao(permissao);
    }
}