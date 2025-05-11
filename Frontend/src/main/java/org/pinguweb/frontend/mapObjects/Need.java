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
    public int pushToServer(){
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
        return 0;
    }

    @Override
    public int deleteFromServer() {
        String finurl = "/api/needs/" + this.getID();
        try{
            HttpStatusCode status = BackendService.deleteFromBackend(finurl);
            if (status == HttpStatus.OK){
                //TODO: Eliminar del mapa
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }

    //TODO Comprobar si aquí la url es api zones o api needs
    @Override
    public int updateToServer() {
        String finurl = "/api/needs/" + this.getID();
        try{
            HttpStatusCode status = BackendService.putToBackend(finurl, this);
            if (status == HttpStatus.OK){

            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }
}
