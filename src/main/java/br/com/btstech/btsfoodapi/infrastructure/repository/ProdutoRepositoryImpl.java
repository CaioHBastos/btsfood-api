package br.com.btstech.btsfoodapi.infrastructure.repository;

import br.com.btstech.btsfoodapi.domain.model.FotoProduto;
import br.com.btstech.btsfoodapi.domain.repository.ProdutoRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public FotoProduto save(FotoProduto fotoProduto) {
        return entityManager.merge(fotoProduto);
    }

    @Transactional
    @Override
    public void delete(FotoProduto fotoProduto) {
        entityManager.remove(fotoProduto);
    }
}
