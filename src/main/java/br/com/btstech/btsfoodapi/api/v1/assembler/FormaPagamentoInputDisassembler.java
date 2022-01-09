package br.com.btstech.btsfoodapi.api.v1.assembler;

import br.com.btstech.btsfoodapi.api.v1.model.input.FormaPagamentoInput;
import br.com.btstech.btsfoodapi.domain.model.FormaPagamento;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FormaPagamentoInputDisassembler {

    private ModelMapper modelMapper;

    public FormaPagamento toDomainObject(FormaPagamentoInput formaPagamentoInput) {
        return modelMapper.map(formaPagamentoInput, FormaPagamento.class);
    }

    public void copyToDomainObject(FormaPagamentoInput formaPagamentoInput, FormaPagamento formaPagamento) {
        modelMapper.map(formaPagamentoInput, formaPagamento);
    }
}
