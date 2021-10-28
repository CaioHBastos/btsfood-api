package br.com.btstech.btsfoodapi.domain.service;

import br.com.btstech.btsfoodapi.domain.filter.VendaDiarioFilter;
import br.com.btstech.btsfoodapi.domain.model.dto.VendaDiaria;

import java.util.List;

public interface VendaQueryService {

    List<VendaDiaria> consultarVendasDiarias(VendaDiarioFilter filtro, String timeOffset);
}
