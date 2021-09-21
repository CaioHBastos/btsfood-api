package br.com.btstech.btsfoodapi.infrastructure.repository.spec;

import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestauranteSpecs {

    public static Specification<Restaurante> comFreteGratis() {
        return (root, query, builder) ->
                builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
    }

    public static Specification<Restaurante> comNomeSemelhante(String nome) {
        return (root, criteriaQuery, builder) ->
                builder.like(root.get("nome"), "%" + nome + "%");
    }
}
