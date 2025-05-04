package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.NeedDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Setter
@Getter
public class Need extends MapObject{

    LMarker markerObj;

    public Need(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        this.markerObj = new LMarker(reg, new LLatLng(reg, latitude, longitude));
    }


    @Override
    public void addToMap(LMap map){
        this.getMarkerObj().addTo(map);
    }

    @Override
    public void removeFromMap(LMap map){
        this.getMarkerObj().removeFrom(map);
    }

    @Override
    public void pushToServer(){
        NeedDTO needDTO = new NeedDTO();

        // TODO: AGREGAR LO QUE QUIERAS GUARDAR

        String finurl = "/api/needs";
        try{
            BackendObject<NeedDTO> status = BackendService.postToBackend(finurl, needDTO, NeedDTO.class);
            if (status.getStatusCode() == HttpStatus.OK){
                //TODO: Se añadio exitosamente
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
    }

    @Override
    public void deleteFromServer() {
        String finurl = "/api/zones/" + this.getID();
        try{
            HttpStatusCode status = BackendService.deleteFromBackend(finurl);
            if (status == HttpStatus.OK){
                //TODO: Eliminar del mapa
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
    }

    public static List<NeedDTO> getAllFromServer() {
        BackendObject<List<NeedDTO>> needs = BackendService.getListFromBackend(BackendService.BACKEND + "/api/needs",
                new ParameterizedTypeReference<>() {
                });

        if (needs.getStatusCode() == HttpStatus.OK){
            return needs.getData();
        }
        else if (needs.getStatusCode() == HttpStatus.NO_CONTENT){
            log.error("FALLO: No se ha encontrado contenido en la petición: /api/needs");
            return new ArrayList<>();
        }
        else if (needs.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE){
            log.error("FALLO: Petición /api/needs devolvió servicio no disponible. ¿El backend funciona?");
            return new ArrayList<>();
        }
        else{
            throw new RuntimeException("Backend object return unexpected status code");
        }
    }
}
