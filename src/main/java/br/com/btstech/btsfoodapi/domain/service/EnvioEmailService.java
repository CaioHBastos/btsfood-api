package br.com.btstech.btsfoodapi.domain.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Map;
import java.util.Set;

public interface EnvioEmailService {

    void enviar(Mensagem mensagem);

    @Getter
    @Builder
    class Mensagem {

        @Singular
        private Set<String> destinatarios;

        @NonNull
        private String corpo;

        @NonNull
        private String assunto;

        @Singular("variavel")
        private Map<String, Object> variaveis;
    }
}
