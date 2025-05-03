package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.TaskDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
public class Volunteer{

    public static List<TaskDTO> getAllFromServer() {
        BackendObject<List<TaskDTO>> tasks = BackendService.getListFromBackend(BackendService.BACKEND + "/api/tasks",
                new ParameterizedTypeReference<>() {
                });

        if (tasks.getStatusCode() == HttpStatus.OK) {
            return tasks.getData();
        } else if (tasks.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.error("FALLO: No se ha encontrado contenido en la petición: /api/tasks");
            return new ArrayList<>();
        } else if (tasks.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            log.error("FALLO: Petición /api/zones devolvió servicio no disponible. ¿El backend funciona?");
            return new ArrayList<>();
        } else {
            throw new RuntimeException("Backend object return unexpected status code");
        }
    }
}
