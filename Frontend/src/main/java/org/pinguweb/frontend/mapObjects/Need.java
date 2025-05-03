package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import org.pingu.domain.DTO.NeedDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

@Setter
@Getter
public class Need extends MapObject{

    LMarker markerObj;

    public Need(LComponentManagementRegistry reg, Double latitude, Double longitude){
        this.setLatitude(latitude);
        this.setLongitude(longitude);
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
