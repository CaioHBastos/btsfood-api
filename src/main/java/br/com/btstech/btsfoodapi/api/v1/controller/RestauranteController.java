package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.v1.assembler.RestauranteApenasNomeModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.RestauranteBasicoModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.RestauranteInputDisassembler;
import br.com.btstech.btsfoodapi.api.v1.assembler.RestauranteModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.model.RestauranteApenasNomeModel;
import br.com.btstech.btsfoodapi.api.v1.model.RestauranteBasicoModel;
import br.com.btstech.btsfoodapi.api.v1.model.RestauranteModel;
import br.com.btstech.btsfoodapi.api.v1.model.input.RestauranteInput;
import br.com.btstech.btsfoodapi.api.v1.openapi.controller.RestauranteControllerOpenApi;
import br.com.btstech.btsfoodapi.core.security.CheckSecurity;
import br.com.btstech.btsfoodapi.domain.exception.CidadeNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.exception.CozinhaNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.exception.NegocioException;
import br.com.btstech.btsfoodapi.domain.exception.RestauranteNaoEncontradoException;
import br.com.btstech.btsfoodapi.domain.model.Restaurante;
import br.com.btstech.btsfoodapi.domain.repository.RestauranteRepository;
import br.com.btstech.btsfoodapi.domain.service.CadastroRestauranteService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/v1/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements RestauranteControllerOpenApi {

    private RestauranteRepository restauranteRepository;
    private CadastroRestauranteService cadastroRestauranteService;
    private RestauranteModelAssembler restauranteModelAssembler;
    private RestauranteInputDisassembler restauranteInputDisassembler;
    private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;
    private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler;

    //@JsonView(RestauranteView.Resumo.class)
    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping
    public CollectionModel<RestauranteBasicoModel> listar() {
        return restauranteBasicoModelAssembler
                .toCollectionModel(restauranteRepository.findAll());
    }

    //@JsonView(RestauranteView.ApenasNome.class)
    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping(params = "projecao=apenas-nome")
    public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
        return restauranteApenasNomeModelAssembler
                .toCollectionModel(restauranteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteModel> buscar(@PathVariable Long id) {
        Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(id);

        RestauranteModel restauranteModel = restauranteModelAssembler.toModel(restaurante);
        return ResponseEntity.ok(restauranteModel);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
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

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
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

        } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException exception) {
            throw new NegocioException(exception.getMessage());
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PutMapping("/{id}/ativo")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        cadastroRestauranteService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/ativo")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        cadastroRestauranteService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @PutMapping("/{id}/abertura")
    public ResponseEntity<Void> abrir(@PathVariable("id") Long restauranteId) {
        cadastroRestauranteService.abrir(restauranteId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @Override
    @PutMapping("/{id}/fechamento")
    public ResponseEntity<Void> fechar(@PathVariable("id") Long restauranteId) {
        cadastroRestauranteService.fechar(restauranteId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @PutMapping("/ativacoes")
    public ResponseEntity<Void> ativarMultiplos(@RequestBody List<Long> restauranteIds) {
        try {
            cadastroRestauranteService.ativar(restauranteIds);
            return ResponseEntity.noContent().build();

        } catch (RestauranteNaoEncontradoException exception) {
            throw new NegocioException(exception.getMessage(), exception);
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @Override
    @DeleteMapping("/ativacoes")
    public ResponseEntity<Void> inativarMultiplos(@RequestBody List<Long> restauranteIds) {
        try {
            cadastroRestauranteService.inativar(restauranteIds);
            return ResponseEntity.noContent().build();

        } catch (RestauranteNaoEncontradoException exception) {
            throw new NegocioException(exception.getMessage(), exception);
        }
    }


    /*@GetMapping
    public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
        List<Restaurante> restaurantes = restauranteRepository.findAll();
        List<RestauranteModel> restauranteModels = restauranteModelAssembler.toCollectionModel(restaurantes);

        MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restauranteModels);
        restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);

        if ("completo".equalsIgnoreCase(projecao)) {
            restaurantesWrapper.setSerializationView(null);
        }

        return restaurantesWrapper;
    }*/

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

