package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.CatastropheDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Slf4j
public class Catastrophe{

    public static List<CatastropheDTO> getAllFromServer() {
        BackendObject<List<CatastropheDTO>> catastrophes = BackendService.getListFromBackend(BackendService.BACKEND + "/api/catastrophes",
                new ParameterizedTypeReference<>() {
                });

        if (catastrophes.getStatusCode() == HttpStatus.OK) {
            return catastrophes.getData();
        } else if (catastrophes.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.error("FALLO: No se ha encontrado contenido en la petición: /api/catastrophes");
            return new ArrayList<>();
        } else if (catastrophes.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            log.error("FALLO: Petición /api/catastrophes devolvió servicio no disponible. ¿El backend funciona?");
            return new ArrayList<>();
        } else {
            throw new RuntimeException("Backend object return unexpected status code");
        }
    }
}
