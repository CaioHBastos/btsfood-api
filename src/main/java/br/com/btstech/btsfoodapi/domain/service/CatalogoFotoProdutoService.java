package br.com.btstech.btsfoodapi.domain.service;

import br.com.btstech.btsfoodapi.domain.model.FotoProduto;
import br.com.btstech.btsfoodapi.domain.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CatalogoFotoProdutoService {

    private ProdutoRepository produtoRepository;

    @Transactional
    public FotoProduto salvar(FotoProduto fotoProduto) {
        Long restauranteId = fotoProduto.getRestauranteId();
        Long produtoId = fotoProduto.getProduto().getId();

        Optional<FotoProduto> fotoExistente =
                produtoRepository.findFotoById(restauranteId, produtoId);

        if (fotoExistente.isPresent()) {
            produtoRepository.delete(fotoExistente.get());
        }

        return produtoRepository.save(fotoProduto);
    }
}
