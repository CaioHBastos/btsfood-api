package br.com.btstech.btsfoodapi.core.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface CheckSecurity {

    @interface Cozinhas {

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_COZINHAS')")
        @interface PodeEditar {
        }

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        @interface PodeConsultar {
        }
    }

    @interface Restaurantes {

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_RESTAURANTES')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface PodeGerenciarCadastro { }

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and " +
                "(hasAuthority('EDITAR_RESTAURANTES') or " +
                "@btsSecurity.gerenciaRestaurante(#restauranteId))")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface PodeGerenciarFuncionamento { }

        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @interface PodeConsultar { }

    }
}
