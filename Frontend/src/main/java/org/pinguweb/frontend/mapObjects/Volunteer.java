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
import org.springframework.http.HttpStatusCode;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolygon;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolylineOptions;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Getter
public class Volunteer{

    public static List<VolunteerDTO> getAllFromServer() {
        BackendObject<List<VolunteerDTO>> volunteer = BackendService.getListFromBackend(BackendService.BACKEND + "/api/volunteers",
                new ParameterizedTypeReference<>() {
                });

        if (volunteer.getStatusCode() == HttpStatus.OK) {
            return volunteer.getData();
        } else if (volunteer.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.error("FALLO: No se ha encontrado contenido en la petición: /api/volunteers");
            return new ArrayList<>();
        } else if (volunteer.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            log.error("FALLO: Petición /api/zones devolvió servicio no disponible. ¿El backend funciona?");
            return new ArrayList<>();
        } else {
            throw new RuntimeException("Backend object return unexpected status code");
        }
    }
}
