package br.com.btstech.btsfoodapi.api.model.input;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EstadoIdInput {

    @ApiModelProperty(example = "Taboão da Serra", required = true)
    @NotNull
    private Long id;
}
