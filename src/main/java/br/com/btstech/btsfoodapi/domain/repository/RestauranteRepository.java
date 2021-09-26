package br.com.btstech.btsfoodapi.domain.repository;

import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestauranteRepository
        extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries,
        JpaSpecificationExecutor<Restaurante> {

//    @Query("from Restaurante r join fetch r.cozinha left join fetch r.formasPagamento")
//    List<Restaurante> findAll();

    /**
     * Busca uma lista de restaurante que a taxa frete esteja
     * entre o valor inicial e o final.
     *
     * @param taxaInicial {@code BigDecimal}
     *      - Valor da taxa inicial para a busca
     * @param taxaFinal {@code BigDecimal}
     *      - Valor da taxa final para a busca
     * @return {@code List<Restaurante>}
     *      - Retorna uma lista de restaurantes da busca na base de dados
     *      que tem a taxa frente entre o valor da taxa inicial e final indicada.
     */
    List<Restaurante> queryByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

    /**
     * Busca uma lista de restaurante contendo alguma informação que foi passada
     * no parametro 'nome' e que tenha o id informado no parametro.
     *
     * @param nome {@code nome}
     *      - valor do nome do restaurante
     * @param cozinhaId {@code Long}
     *      - valor do ID da cozinha que está relacionado ao restaurante
     * @return {@code List<Restaurante>}
     *      - Retorna uma lista de restaurante no qual contem alguma informação
     *      passada no nome E tenha o id da cozinha informado.
     */
    //List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);

    //@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")

    /**
     * Busca uma lista de restaurante contendo alguma informação que foi passada
     * no parametro 'nome' e que tenha o id informado no parametro.
     *
     * -- QUERY NO ARQUIVO META-INF.orm.xml
     *
     * @param nome {@code nome}
     *      - valor do nome do restaurante
     * @param cozinhaId {@code Long}
     *      - valor do ID da cozinha que está relacionado ao restaurante
     * @return {@code List<Restaurante>}
     *      - Retorna uma lista de restaurante no qual contem alguma informação
     *      passada no nome E tenha o id da cozinha informado.
     */
    List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinhaId);

    /**
     * Busca o primeiro restaurante no qual contem algum restaurante no qual
     * a informação do parametro esteja no nome.
     *
     * @param nome {@code nome}
     *      - valor do nome do restaurante
     * @return {@code Optional<Restaurante>}
     *      - Retorna o valor opcional do primeiro restaurante que retornou
     *      na busca.
     */
    Optional<Restaurante> findFirstRestauranteByNomeContaining(String nome);

    /**
     * Busca uma lista dos primeiros dois restaurantes no qual contem o nome
     * informado no parametro.
     *
     * @param nome {@code nome}
     *      - valor do nome do restaurante
     * @return {@code List<Restaurante>}
     *      - Retorna uma lista dos dois primeiros restaurantes
     *       por nome.
     */
    List<Restaurante> findTop2ByNomeContaining(String nome);

    /**
     * Busca a quantidade de restaurantes que tem relação com a cozinha informada.
     *
     * @param cozinhaId {@code Long}
     *      - valor do ID da cozinha que está relacionado ao restaurante
     * @return  {@code int}
     *      - retorna a quantidade de restaurantes que tenham a cozinha informada.
     */
    int countByCozinhaId(Long cozinhaId);
}
