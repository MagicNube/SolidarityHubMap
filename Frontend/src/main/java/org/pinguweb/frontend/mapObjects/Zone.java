package org.pinguweb.frontend.mapObjects;

import lombok.Getter;
import lombok.Setter;
import org.pingu.domain.DTO.ZoneDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
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

@Setter
@Getter
public class Zone extends MapObject{

    private String name;
    private String description;
    private String emergencyLevel;
    private int catastrophe;
    private List<Integer> storages;
    private List<Double> latitudes;
    private List<Double> longitudes;

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
        zoneDTO.setName(this.name);
        zoneDTO.setDescription(this.description);
        zoneDTO.setEmergencyLevel(this.emergencyLevel);
        zoneDTO.setCatastrophe(this.catastrophe);
        zoneDTO.setStorages(this.storages);
        zoneDTO.setLatitudes(this.latitudes);
        zoneDTO.setLongitudes(this.longitudes);

        // TODO: AGREGAR LO QUE QUIERAS GUARDAR


        String finurl = "/api/zone";
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
        String finurl = "/api/zone/" + this.getID();
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
