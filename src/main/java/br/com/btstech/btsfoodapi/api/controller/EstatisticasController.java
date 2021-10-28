package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.domain.filter.VendaDiarioFilter;
import br.com.btstech.btsfoodapi.domain.model.dto.VendaDiaria;
import br.com.btstech.btsfoodapi.domain.service.VendaQueryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/estatisticas")
public class EstatisticasController {

    private final VendaQueryService vendaQueryService;

    @GetMapping("/vendas-diarias")
    public List<VendaDiaria> consultarVendasDiarias(VendaDiarioFilter filtro,
                                                    @RequestParam(required = false, defaultValue = "+00:00") String timeOffset) {

        return vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
    }
}
