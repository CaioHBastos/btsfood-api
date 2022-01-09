package br.com.btstech.btsfoodapi.domain.service;

import br.com.btstech.btsfoodapi.domain.exception.PermissaoNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.model.Permissao;
import br.com.btstech.btsfoodapi.domain.repository.PermissaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CadastroPermissaoService {

    private PermissaoRepository permissaoRepository;

    public Permissao buscarOuFalhar(Long permissaoId) {
        return permissaoRepository.findById(permissaoId)
                .orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
    }
}
