package br.com.btstech.btsfoodapi.domain.repository;

import br.com.btstech.btsfoodapi.domain.model.Cidade;

import java.util.List;

public interface CidadeRepository {

    List<Cidade> listar();
    Cidade buscar(Long id);
    Cidade salvar(Cidade cidade);
    void remover(Long cidadeId);
}
