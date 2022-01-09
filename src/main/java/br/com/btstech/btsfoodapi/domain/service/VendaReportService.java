package br.com.btstech.btsfoodapi.domain.service;

import br.com.btstech.btsfoodapi.domain.filter.VendaDiarioFilter;

public interface VendaReportService {

    byte[] emitirVendasDiarias(VendaDiarioFilter filtro, String timeOffset);
}
