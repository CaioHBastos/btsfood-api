package br.com.btstech.btsfoodapi.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EstadoInput {

    @NotBlank
    private String nome;
}