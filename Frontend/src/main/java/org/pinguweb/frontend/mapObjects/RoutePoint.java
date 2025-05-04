package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.RoutePointDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Setter
@Getter
public class RoutePoint extends MapObject {

    LMarker markerObj;
    private Double latitude;
    private Double longitude;
    private String routeType;



    public RoutePoint(LComponentManagementRegistry reg, Double latitude, Double longitude) {
        this.setLatitude(latitude);
        this.setLongitude(longitude);

        LIcon icon = new LIcon(reg, new LIconOptions()
                .withIconUrl("https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-yellow.png")
                .withShadowUrl("https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png")
                .withIconSize(new LPoint(reg, 25, 41))
                .withIconAnchor(new LPoint(reg, 12, 41))
                .withPopupAnchor(new LPoint(reg, 1, -34))
                .withShadowSize(new LPoint(reg, 41, 41))
        );

        LMarkerOptions options = new LMarkerOptions().withDraggable(true).withIcon(icon);

        this.markerObj = new LMarker(reg, new LLatLng(reg, this.getLatitude(), this.getLongitude()), options);
    }

    @Override
    public void addToMap(LMap map) {
        this.getMarkerObj().addTo(map);
    }

    @Override
    public void removeFromMap(LMap map) {
        this.getMarkerObj().removeFrom(map);
    }

    @Override
    public void pushToServer() {
        RoutePointDTO routePointDTO = new RoutePointDTO();
        routePointDTO.setLatitude(this.getLatitude());
        routePointDTO.setLongitude(this.getLongitude());
        routePointDTO.setRouteType(this.getRouteType());

        // TODO: AGREGAR LO QUE QUIERAS GUARDAR

        String finurl = "/api/routepoints";
        try {
            BackendObject<RoutePointDTO> status = BackendService.postToBackend(finurl, routePointDTO, RoutePointDTO.class);
            if (status.getStatusCode() == HttpStatus.OK) {
                //TODO: Se añadio exitosamente
            }
        } catch (Exception e) {
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
    }

    @Override
    public void deleteFromServer() {
        String finurl = "/api/routepoints/" + this.getID();
        try {
            HttpStatusCode status = BackendService.deleteFromBackend(finurl);
            if (status == HttpStatus.OK) {
                //TODO: Eliminar del mapa
            }
        } catch (Exception e) {
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
    }

    public static List<RoutePointDTO> getAllFromServer() {
        BackendObject<List<RoutePointDTO>> routepoints = BackendService.getListFromBackend(BackendService.BACKEND + "/api/routepoints",
                new ParameterizedTypeReference<>() {
                });

        if (routepoints.getStatusCode() == HttpStatus.OK) {
            return routepoints.getData();
        } else if (routepoints.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.error("FALLO: No se ha encontrado contenido en la petición: /api/routepoints");
            return new ArrayList<>();
        } else if (routepoints.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            log.error("FALLO: Petición /api/routepoints devolvió servicio no disponible. ¿El backend funciona?");
            return new ArrayList<>();
        } else {
            throw new RuntimeException("Backend object return unexpected status code");
        }
    }
}