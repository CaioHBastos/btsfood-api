package br.com.btstech.btsfoodapi.api.openapi.model;

import br.com.btstech.btsfoodapi.api.model.CozinhaModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@ApiModel("CozinhasModel")
@Setter
@Getter
public class CozinhasModelOpenApi {

    private CozinhasModelOpenApi.CozinhaEmbeddedModelOpenApi _embedded;
    private Links _links;
    private PageModelOpenApi page;

    @ApiModel("CidadesEmbeddedModel")
    @Data
    public class CozinhaEmbeddedModelOpenApi {

        private List<CozinhaModel> cozinhas;

    }

}
