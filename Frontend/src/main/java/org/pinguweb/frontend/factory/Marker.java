package org.pinguweb.frontend.factory;

import lombok.Getter;
import lombok.Setter;
import org.pinguweb.DTO.DTO;
import org.pinguweb.DTO.NeedDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import software.xdev.vaadin.maps.leaflet.basictypes.LIcon;
import software.xdev.vaadin.maps.leaflet.basictypes.LIconOptions;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.basictypes.LPoint;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarkerOptions;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

@Setter
@Getter
public class Marker extends MapObject{

    LMarker markerObj;

    public Marker(LComponentManagementRegistry reg, Double latitude, Double longitude){
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.markerObj = new LMarker(reg, new LLatLng(reg, latitude, longitude));
    }

    public Marker convertToZoneMarker(LComponentManagementRegistry reg){
        LIcon icon = new LIcon(reg, new LIconOptions()
                .withIconUrl("https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png")
                .withShadowUrl("https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png")
                .withIconSize(new LPoint(reg, 25, 41))
                .withIconAnchor(new LPoint(reg, 12, 41))
                .withPopupAnchor(new LPoint(reg, 1, -34))
                .withShadowSize(new LPoint(reg, 41, 41))
        );

        LMarkerOptions options = new LMarkerOptions().withDraggable(true).withIcon(icon);

        this.markerObj = new LMarker(reg, new LLatLng(reg, this.getLatitude(), this.getLongitude()), options);
        return this;
    }

    public void addToMap(LMap map){
        this.getMarkerObj().addTo(map);
    }

    @Override
    public void pushToServer(String url, DTO dto){
        NeedDTO needDTO = (NeedDTO) dto;
        String finurl = "/api/need/" + needDTO.getId();
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
    public void deleteFromServer(String url, DTO dto) {
        NeedDTO needDTO = (NeedDTO) dto;
        String finurl = "/api/need/" + needDTO.getId();
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
