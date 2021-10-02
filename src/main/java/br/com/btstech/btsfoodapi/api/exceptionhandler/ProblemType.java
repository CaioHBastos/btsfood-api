package br.com.btstech.btsfoodapi.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio");

    private String uri;
    private String title;

    ProblemType(String path, String title) {
        this.uri = "https://btsfood.com.br" + path;
        this.title = title;
    }
}
