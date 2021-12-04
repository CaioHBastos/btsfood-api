package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.openapi.controller.EstatisticasControllerOpenApi;
import br.com.btstech.btsfoodapi.domain.filter.VendaDiarioFilter;
import br.com.btstech.btsfoodapi.domain.model.dto.VendaDiaria;
import br.com.btstech.btsfoodapi.domain.service.VendaQueryService;
import br.com.btstech.btsfoodapi.domain.service.VendaReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/estatisticas")
public class EstatisticasController implements EstatisticasControllerOpenApi {

    private final VendaQueryService vendaQueryService;
    private VendaReportService vendaReportService;

    @Override
    @GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VendaDiaria> consultarVendasDiarias(VendaDiarioFilter filtro,
                                                    @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {
        return vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
    }

    @Override
    @GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> consultarVendasDiariasPdf(VendaDiarioFilter filtro,
                                                            @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {

        byte[] bytesPdf = vendaReportService.emitirVendasDiarias(filtro, timeOffset);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(headers)
                .body(bytesPdf);
    }
}
