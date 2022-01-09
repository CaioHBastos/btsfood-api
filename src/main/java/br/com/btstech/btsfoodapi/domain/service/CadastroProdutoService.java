package br.com.btstech.btsfoodapi.domain.service;

import br.com.btstech.btsfoodapi.domain.exception.ProdutoNaoEncontradoException;
import br.com.btstech.btsfoodapi.domain.model.Produto;
import br.com.btstech.btsfoodapi.domain.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CadastroProdutoService {

    private ProdutoRepository produtoRepository;

    @Transactional
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto buscarOuFalhar(Long restauranteId, Long produtoId) {
        return produtoRepository.findById(restauranteId, produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
    }
}
