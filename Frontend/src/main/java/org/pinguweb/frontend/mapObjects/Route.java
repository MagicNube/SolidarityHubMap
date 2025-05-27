package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.RouteDTO;
import org.pingu.web.BackendObject;
import org.pingu.web.BackendService;
import org.pinguweb.frontend.services.BackendDTOService;
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
    private List<Integer> pointsID;

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

    public RouteDTO toDto(){
        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setName(this.name);
        routeDTO.setID(getID());
        routeDTO.setRouteType(this.routeType);
        routeDTO.setCatastrophe(this.catastrophe);
        routeDTO.setPoints(this.pointsID);
        return routeDTO;
    }

    @Override
    public int pushToServer(){
        RouteDTO routeDTO = toDto();

        String finurl = "/api/routes";
        try{
            BackendObject<RouteDTO> status = BackendService.postToBackend(BackendDTOService.BACKEND + finurl, routeDTO, RouteDTO.class);
            if (status.getStatusCode() == HttpStatus.OK){
                this.setID(status.getData().getID());
                return status.getData().getID();
            }
            else if (status.getStatusCode() == HttpStatus.NO_CONTENT){
                log.error("FALLO: No se ha encontrado contenido en la petición: /api/routes");
            }
            else if (status.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE){
                log.error("FALLO: Petición /api/routes devolvió servicio no disponible. ¿El backend funciona?");
            }
            else{
                throw new RuntimeException("Backend object return unexpected status code");
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }

    @Override
    public int deleteFromServer() {
        String finurl = "/api/routes/" + this.getID();
        try{
            HttpStatusCode status = BackendService.deleteFromBackend(BackendDTOService.BACKEND + finurl);
            if (status == HttpStatus.OK){
                return this.getID();
            }
            else if (status == HttpStatus.NO_CONTENT){
                log.error("FALLO: No se ha encontrado contenido en la petición: /api/routes");
            }
            else if (status == HttpStatus.SERVICE_UNAVAILABLE){
                log.error("FALLO: Petición /api/routes devolvió servicio no disponible. ¿El backend funciona?");
            }
            else{
                throw new RuntimeException("Backend object return unexpected status code");
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }

    @Override
    public int updateToServer() {
        RouteDTO routeDTO = toDto();
        String finurl = "/api/routes/" + getID();
        try{
            HttpStatusCode status = BackendService.putToBackend(BackendDTOService.BACKEND + finurl, routeDTO);
            if (status == HttpStatus.OK){
                return this.getID();
            }
            else if (status == HttpStatus.NO_CONTENT){
                log.error("FALLO: No se ha encontrado contenido en la petición: /api/routes");
            }
            else if (status == HttpStatus.SERVICE_UNAVAILABLE){
                log.error("FALLO: Petición /api/routes devolvió servicio no disponible. ¿El backend funciona?");
            }
            else{
                throw new RuntimeException("Backend object return unexpected status code");
            }
        }
        catch (Exception e){
            log.error(e.getMessage(),  Arrays.stream(e.getStackTrace()).toArray());
        }
        return 0;
    }
}
