package br.com.btstech.btsfoodapi.api;

import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@UtilityClass
public class ResourceUriHelper {

    public static URI addUri(Object resourceId) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(resourceId)
                .toUri();
    }
}
