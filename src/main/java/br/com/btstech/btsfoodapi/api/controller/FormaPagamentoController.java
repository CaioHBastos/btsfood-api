package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.FormaPagamentoInputDisassembler;
import br.com.btstech.btsfoodapi.api.assembler.FormaPagamentoModelAssembler;
import br.com.btstech.btsfoodapi.api.model.FormaPagamentoModel;
import br.com.btstech.btsfoodapi.api.model.input.FormaPagamentoInput;
import br.com.btstech.btsfoodapi.domain.model.FormaPagamento;
import br.com.btstech.btsfoodapi.domain.repository.FormaPagamentoRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroFormaPagamentoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    private FormaPagamentoRepository formaPagamentoRepository;
    private CadastroFormaPagamentoService cadastroFormaPagamento;
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
    private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;
    
    @GetMapping
    public List<FormaPagamentoModel> listar() {
        List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();
        
        return formaPagamentoModelAssembler.toCollectionModel(todasFormasPagamentos);
    }
    
    @GetMapping("/{id}")
    public FormaPagamentoModel buscar(@PathVariable Long id) {
        FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(id);
        
        return formaPagamentoModelAssembler.toModel(formaPagamento);
    }
    
    @PostMapping
    public ResponseEntity<FormaPagamentoModel> adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);
        
        formaPagamento = cadastroFormaPagamento.salvar(formaPagamento);
        FormaPagamentoModel formaPagamentoModel = formaPagamentoModelAssembler.toModel(formaPagamento);

        return ResponseEntity.status(HttpStatus.CREATED).body(formaPagamentoModel);
    }
    
    @PutMapping("/{id}")
    public FormaPagamentoModel atualizar(@PathVariable Long id,
            @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamentoAtual = cadastroFormaPagamento.buscarOuFalhar(id);
        
        formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
        
        formaPagamentoAtual = cadastroFormaPagamento.salvar(formaPagamentoAtual);
        
        return formaPagamentoModelAssembler.toModel(formaPagamentoAtual);
    }
    
    @DeleteMapping("/{formaPagamentoId}")
    public ResponseEntity<Void> remover(@PathVariable Long formaPagamentoId) {
        cadastroFormaPagamento.excluir(formaPagamentoId);
        return ResponseEntity.noContent().build();
    }   
}   