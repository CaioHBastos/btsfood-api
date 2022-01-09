package br.com.btstech.btsfoodapi.api.v1.model.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CidadeInput {

    @ApiModelProperty(example = "Taboão da Serra", required = true)
    @NotBlank
    private String nome;

    @Valid
    @NotNull
    private EstadoIdInput estado;
}
