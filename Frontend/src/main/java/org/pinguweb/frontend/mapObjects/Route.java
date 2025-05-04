package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.RouteDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolyline;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolylineOptions;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Setter
@Getter
public class Route extends MapObject{

    List<LLatLng> points;
    LPolyline polygon;

    private String name;
    private String routeType;
    private int catastrophe;

    public Route(){
        this.points = new ArrayList<>();
    }

    public void addPoint(LComponentManagementRegistry reg, Tuple<Double, Double> coord){
        this.points.add(new LLatLng(reg, coord._1(), coord._2()));
    }

    public void generatePolygon(LComponentManagementRegistry reg, String lineColor, String fillColor){
        this.polygon = new LPolyline(reg, points, new LPolylineOptions().withColor(lineColor).withFillColor(fillColor));
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
        RouteDTO routeDTO = new RouteDTO();

        // TODO: AGREGAR LO QUE QUIERAS GUARDAR

        String finurl = "/api/routes";
        try{
            BackendObject<RouteDTO> status = BackendService.postToBackend(finurl, routeDTO, RouteDTO.class);
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
        String finurl = "/api/routes/" + this.getID();
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

    public static List<RouteDTO> getAllFromServer() {
        BackendObject<List<RouteDTO>> routes = BackendService.getListFromBackend(BackendService.BACKEND + "/api/routes",
                new ParameterizedTypeReference<>() {
                });

        if (routes.getStatusCode() == HttpStatus.OK) {
            return routes.getData();
        } else if (routes.getStatusCode() == HttpStatus.NO_CONTENT) {
            log.error("FALLO: No se ha encontrado contenido en la petición: /api/routes");
            return new ArrayList<>();
        } else if (routes.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            log.error("FALLO: Petición /api/routes devolvió servicio no disponible. ¿El backend funciona?");
            return new ArrayList<>();
        } else {
            throw new RuntimeException("Backend object return unexpected status code");
        }
    }
}
