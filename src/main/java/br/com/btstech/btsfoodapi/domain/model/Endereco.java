package br.com.btstech.btsfoodapi.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Embeddable
public class Endereco {

    @Column(name = "endereco_cep")
    private String cep;

    @Column(name = "endereco_logradouro")
    private String logradouro;

    @Column(name = "endereco_numero")
    private String numero;

    @Column(name = "endereco_complemento")
    private String complemento;

    @Column(name = "endereco_bairro")
    private String bairro;

    @ManyToOne
    @JoinColumn(name = "endereco_cidade_id")
    private Cidade cidade;
}
