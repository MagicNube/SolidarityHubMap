package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.NeedDTO;
import org.pingu.web.BackendObject;
import org.pingu.web.BackendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.util.Arrays;

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
        return 0;
    }

    @Override
    public int updateToServer() {
        return 0;
    }
}
