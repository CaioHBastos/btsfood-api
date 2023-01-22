package br.com.btstech.btsfoodapi.api.v1.controller;

import br.com.btstech.btsfoodapi.api.v1.assembler.FotoProdutoModelAssembler;
import br.com.btstech.btsfoodapi.api.v1.model.FotoProdutoModel;
import br.com.btstech.btsfoodapi.api.v1.model.input.FotoProdutoInput;
import br.com.btstech.btsfoodapi.api.v1.openapi.controller.RestauranteProdutoFotoControllerOpenApi;
import br.com.btstech.btsfoodapi.core.security.CheckSecurity;
import br.com.btstech.btsfoodapi.domain.exception.EntidadeNaoEncontradaException;
import br.com.btstech.btsfoodapi.domain.model.FotoProduto;
import br.com.btstech.btsfoodapi.domain.model.Produto;
import br.com.btstech.btsfoodapi.domain.service.CadastroProdutoService;
import br.com.btstech.btsfoodapi.domain.service.CatalogoFotoProdutoService;
import br.com.btstech.btsfoodapi.domain.service.FotoStorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/produtos/{produtoId}/foto",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoFotoController implements RestauranteProdutoFotoControllerOpenApi {

    private CadastroProdutoService cadastroProdutoService;
    private CatalogoFotoProdutoService catalogoFotoProdutoService;
    private FotoProdutoModelAssembler fotoProdutoModelAssembler;
    private FotoStorageService fotoStorageService;

    @CheckSecurity.Restaurantes.PodeConsultar
    @Override
    @GetMapping
    public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);

        return fotoProdutoModelAssembler.toModel(fotoProduto);
    }

    @Override
    @GetMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> servir(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                                    @RequestHeader(name = "Accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {

        try {
            FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);

            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
            List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);

            verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);

            var fotoRecuperada = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());

            if (fotoRecuperada.temUrl()) {
                return ResponseEntity
                        .status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
                        .build();
            } else {

                return ResponseEntity.ok()
                        .contentType(mediaTypeFoto)
                        .body(new InputStreamResource(fotoRecuperada.getInputStream()));
            }


        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CheckSecurity.Restaurantes.PodeEditar
    @Override
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                                          @Valid FotoProdutoInput fotoProdutoInput,
                                          @RequestPart MultipartFile arquivo) throws IOException {

        Produto produto = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
        //MultipartFile arquivo = fotoProdutoInput.getArquivo();

        FotoProduto fotoProduto = new FotoProduto();
        fotoProduto.setProduto(produto);
        fotoProduto.setDescricao(fotoProdutoInput.getDescricao());
        fotoProduto.setContentType(arquivo.getContentType());
        fotoProduto.setTamanho(arquivo.getSize());
        fotoProduto.setNomeArquivo(arquivo.getOriginalFilename());

        FotoProduto fotoSalva = catalogoFotoProdutoService.salvar(fotoProduto, arquivo.getInputStream());

        return fotoProdutoModelAssembler.toModel(fotoSalva);
    }

    @CheckSecurity.Restaurantes.PodeEditar
    @Override
    @DeleteMapping
    public ResponseEntity<Void> excluir(@PathVariable Long restauranteId, @PathVariable Long produtoId) {

        catalogoFotoProdutoService.excluir(restauranteId, produtoId);

        return ResponseEntity.noContent().build();
    }

    private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {

        boolean compativel = mediaTypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
        }
    }
}
