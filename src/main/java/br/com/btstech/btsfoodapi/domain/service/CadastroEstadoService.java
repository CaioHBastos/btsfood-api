package br.com.btstech.btsfoodapi.domain.service;

import br.com.btstech.btsfoodapi.domain.exception.EntidadeEmUsoException;
import br.com.btstech.btsfoodapi.domain.exception.EntidadeNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.model.Estado;
import br.com.btstech.btsfoodapi.domain.repository.EstadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CadastroEstadoService {

    private EstadoRepository estadoRepository;

    public Estado salvar(Estado estado) {
        return estadoRepository.salvar(estado);
    }

    public void excluir(Long estadoId) {
        try {
            estadoRepository.remover(estadoId);

        } catch (EmptyResultDataAccessException exception) {
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de estado com o código %d", estadoId));

        } catch (DataIntegrityViolationException exception) {
            throw new EntidadeEmUsoException(
                    String.format("Estado de código %d não pode ser removida, pois está em uso", estadoId));
        }
    }
}
