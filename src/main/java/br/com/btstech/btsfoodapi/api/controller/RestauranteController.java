package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.domain.exception.EntidadeNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import br.com.btstech.btsfoodapi.domain.repository.RestauranteRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

    private RestauranteRepository restauranteRepository;
    private CadastroRestauranteService cadastroRestauranteService;

    @GetMapping
    public List<Restaurante> listar() {
        return restauranteRepository.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> buscar(@PathVariable Long id) {
        Restaurante restaurante = restauranteRepository.buscar(id);

        if (restaurante != null) {
            return ResponseEntity.ok(restaurante);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Restaurante NovoRestaurante) {
        try {
            Restaurante restaurante = cadastroRestauranteService.salvar(NovoRestaurante);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(restaurante);

        } catch (EntidadeNaoEncontradaException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Restaurante novoRestaurante) {
        try {
            Restaurante restauranteAtual = restauranteRepository.buscar(id);

            if (restauranteAtual == null) {
                return ResponseEntity.notFound().build();
            }

            BeanUtils.copyProperties(novoRestaurante, restauranteAtual, "id");

            cadastroRestauranteService.salvar(restauranteAtual);
            return ResponseEntity.ok(restauranteAtual);

        } catch (EntidadeNaoEncontradaException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        Restaurante restauranteAtual = restauranteRepository.buscar(id);

        if (restauranteAtual == null) {
            return ResponseEntity.notFound().build();
        }

        merge(campos, restauranteAtual);

        return atualizar(id, restauranteAtual);
    }

    private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
            Field campo = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
            campo.setAccessible(true);

            Object novoValor = ReflectionUtils.getField(campo, restauranteOrigem);

            ReflectionUtils.setField(campo, restauranteDestino, novoValor);
        });
    }
}

