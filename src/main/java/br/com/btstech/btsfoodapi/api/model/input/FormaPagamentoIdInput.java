package br.com.btstech.btsfoodapi.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class FormaPagamentoIdInput {

    @NotNull
    private Long id;
}
