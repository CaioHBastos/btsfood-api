package br.com.btstech.btsfoodapi.core.security;

import br.com.btstech.btsfoodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class BtsSecurity {

    @Autowired
    private RestauranteRepository restauranteRepository;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getUsuarioId() {
        Jwt jwt = (Jwt) getAuthentication().getPrincipal();

        return jwt.getClaim("usuario_id");
    }

    public boolean gerenciaRestaurante(Long restauranteId) {
        return restauranteRepository.existsResponsavel(restauranteId, getUsuarioId());
    }
}
