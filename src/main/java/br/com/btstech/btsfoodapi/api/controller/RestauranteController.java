package br.com.btstech.btsfoodapi.api.controller;

import br.com.btstech.btsfoodapi.api.assembler.RestauranteInputDisassembler;
import br.com.btstech.btsfoodapi.api.assembler.RestauranteModelAssembler;
import br.com.btstech.btsfoodapi.api.model.RestauranteModel;
import br.com.btstech.btsfoodapi.api.model.input.RestauranteInput;
import br.com.btstech.btsfoodapi.domain.exception.CozinhaNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.exception.NegocioException;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import br.com.btstech.btsfoodapi.domain.repository.RestauranteRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroRestauranteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

    private RestauranteRepository restauranteRepository;
    private CadastroRestauranteService cadastroRestauranteService;
    private RestauranteModelAssembler restauranteModelAssembler;
    private RestauranteInputDisassembler restauranteInputDisassembler;

    @GetMapping
    public List<RestauranteModel> listar() {
        List<Restaurante> restaurantes = restauranteRepository.findAll();

        return restauranteModelAssembler.toCollectionModel(restaurantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteModel> buscar(@PathVariable Long id) {
        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(id);

        RestauranteModel restauranteModel = restauranteModelAssembler.toModel(restaurante);
        return ResponseEntity.ok(restauranteModel);
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
        try {
            Restaurante novoRestaurante = restauranteInputDisassembler.toDomain(restauranteInput);

            Restaurante restaurante = cadastroRestauranteService.salvar(novoRestaurante);

            RestauranteModel restauranteModel = restauranteModelAssembler.toModel(restaurante);
            return ResponseEntity.status(HttpStatus.CREATED).body(restauranteModel);

        } catch (CozinhaNaoEncontradaException exception) {
            throw new NegocioException(exception.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteInput restauranteInput) {
        //Restaurante novoRestaurante = restauranteInputDisassembler.toDomain(restauranteInput);

        Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(id);

        restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);

        /*BeanUtils.copyProperties(novoRestaurante, restauranteAtual,
                "id", "formasPagamento", "endereco", "dataCadastro", "produtos");*/

        try {
            Restaurante restauranteSalvo = cadastroRestauranteService.salvar(restauranteAtual);

            RestauranteModel restauranteModel = restauranteModelAssembler.toModel(restauranteSalvo);
            return ResponseEntity.ok(restauranteModel);

        } catch (CozinhaNaoEncontradaException exception) {
            throw new NegocioException(exception.getMessage());
        }
    }

    /*@PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos,
                                              HttpServletRequest request) {
        Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(id);

        merge(campos, restauranteAtual, request);
        validate(restauranteAtual, "restaurante");

        return atualizar(id, restauranteAtual);
    }

    private void validate(Restaurante restaurante, String objectName) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
        smartValidator.validate(restaurante, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidacaoException(bindingResult);
        }
    }

    private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {

        ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

            dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
                Field campo = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
                campo.setAccessible(true);

                Object novoValor = ReflectionUtils.getField(campo, restauranteOrigem);

                ReflectionUtils.setField(campo, restauranteDestino, novoValor);
            });

        } catch (IllegalArgumentException ex) {
            Throwable rootCause = ExceptionUtils.getRootCause(ex);
            throw new HttpMessageNotReadableException(ex.getMessage(), rootCause, servletServerHttpRequest);
        }
    }*/
}

