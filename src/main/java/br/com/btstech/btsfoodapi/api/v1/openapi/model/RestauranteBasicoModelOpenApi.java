package br.com.btstech.btsfoodapi.api.v1.openapi.model;

import br.com.btstech.btsfoodapi.api.v1.model.CozinhaModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@ApiModel("RestaurantesBasicoModel")
@Getter
@Setter
public class RestauranteBasicoModelOpenApi {

    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "Thai Gourmet")
    private String nome;

    @ApiModelProperty(example = "12.00")
    private BigDecimal taxaFrete;

    private CozinhaModel cozinha;
}
