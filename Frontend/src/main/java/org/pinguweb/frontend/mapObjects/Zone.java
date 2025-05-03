package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.RoutePointDTO;
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
public class Zone extends MapObject{

    List<LLatLng> points;
    LPolygon polygon;

    public Zone(){
        this.points = new ArrayList<>();
    }

    public void addPoint(LComponentManagementRegistry reg, Tuple<Double, Double> coord){
        this.points.add(new LLatLng(reg, coord._1(), coord._2()));
    }

    public void generatePolygon(LComponentManagementRegistry reg, String lineColor, String fillColor){
        this.polygon = new LPolygon(reg, points, new LPolylineOptions().withColor(lineColor).withFillColor(fillColor));
    }

    @Override
    public void addToMap(LMap map){
        this.getPolygon().addTo(map);
    }

    @Override
    public void removeFromMap(LMap map){
        this.getPolygon().removeFrom(map);
    }

    @Override
    public void pushToServer(){
        ZoneDTO zoneDTO = new ZoneDTO();

        // TODO: AGREGAR LO QUE QUIERAS GUARDAR

        String finurl = "/api/zones";
        try{
            BackendObject<ZoneDTO> status = BackendService.postToBackend(finurl, zoneDTO, ZoneDTO.class);
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

    public static List<ZoneDTO> getAllFromServer() {
        BackendObject<List<ZoneDTO>> zones = BackendService.getListFromBackend(BackendService.BACKEND + "/api/zones",
                new ParameterizedTypeReference<>() {
                });

        if (zones.getStatusCode() == HttpStatus.OK) {
            return zones.getData();
        } else if (zones.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.error("FALLO: No se ha encontrado contenido en la petición: /api/zones");
            return new ArrayList<>();
        } else if (zones.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            log.error("FALLO: Petición /api/zones devolvió servicio no disponible. ¿El backend funciona?");
            return new ArrayList<>();
        } else {
            throw new RuntimeException("Backend object return unexpected status code");
        }
    }
}
