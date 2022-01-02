package br.com.btstech.btsfoodapi.api.openapi.controller;

import br.com.btstech.btsfoodapi.api.controller.EstatisticasController;
import br.com.btstech.btsfoodapi.domain.filter.VendaDiarioFilter;
import br.com.btstech.btsfoodapi.domain.model.dto.VendaDiaria;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Estatísticas")
public interface EstatisticasControllerOpenApi {

    @ApiOperation(value = "Estatísticas", hidden = true)
    EstatisticasController.EstatisticasModel estatisticas();

    @ApiOperation("Consulta estatísticas de vendas diárias")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "restauranteId", value = "ID do restaurante",
                    example = "1", dataType = "int"),
            @ApiImplicitParam(name = "dataCriacaoInicio", value = "Data/hora inicial da criação do pedido",
                    example = "2019-12-01T00:00:00Z", dataType = "date-time"),
            @ApiImplicitParam(name = "dataCriacaoFim", value = "Data/hora final da criação do pedido",
                    example = "2019-12-02T23:59:59Z", dataType = "date-time")
    })
    List<VendaDiaria> consultarVendasDiarias(
            VendaDiarioFilter filtro,

            @ApiParam(value = "Deslocamento de horário a ser considerado na consulta em relação ao UTC",
                    defaultValue = "+00:00")
                    String timeOffset);

    ResponseEntity<byte[]> consultarVendasDiariasPdf(
            VendaDiarioFilter filtro,
            String timeOffset);
}
