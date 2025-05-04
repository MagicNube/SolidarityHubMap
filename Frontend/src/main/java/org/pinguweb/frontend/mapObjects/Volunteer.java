package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.TaskDTO;
import org.pingu.domain.DTO.VolunteerDTO;
import org.pingu.domain.DTO.ZoneDTO;
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

    public static List<VolunteerDTO> getAllFromServer() {
        BackendObject<List<VolunteerDTO>> volunteers = BackendService.getListFromBackend(BackendService.BACKEND + "/api/volunteers",
                new ParameterizedTypeReference<>() {
                });

        if (volunteers.getStatusCode() == HttpStatus.OK) {
            return volunteers.getData();
        } else if (volunteers.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.error("FALLO: No se ha encontrado contenido en la petición: /api/volunteers");
            return new ArrayList<>();
        } else if (volunteers.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            log.error("FALLO: Petición /api/volunteers devolvió servicio no disponible. ¿El backend funciona?");
            return new ArrayList<>();
        } else {
            throw new RuntimeException("Backend object return unexpected status code");
        }
    }
}
