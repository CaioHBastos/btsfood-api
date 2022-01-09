package br.com.btstech.btsfoodapi.domain.repository;

import br.com.btstech.btsfoodapi.domain.model.Cozinha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long> {

    /**
     * Buscar todas as cozinhas que tem contem o nome informado no parametro.
     *
     * @param nome {@code String}
     *      - valor do nome da cozinha.
     * @return {@code List<Cozinha>}
     *      - Retorna uma lista de todas as cozinhas no qual contem o nome
     *      informado no parametro.
     */
    List<Cozinha> findTodasByNomeContaining(String nome);

    /**
     * Verifica se a cozinha com aquele nome existe na busca na base de dados.
     *
     * @param nome {@code String}
     *      - valor do nome da cozinha.
     * @return {@code boolean}
     *      - Retorna o valor <b>true</b> ou <b>false</b> se a cozinha existe.
     */
    boolean existsByNome(String nome);
}
