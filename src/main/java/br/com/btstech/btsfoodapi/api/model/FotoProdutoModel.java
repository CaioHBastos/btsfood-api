package br.com.btstech.btsfoodapi.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoProdutoModel {

    private String nomeArquivo;
    private String descricao;
    private String contentType;
    private String tamanho;
}
