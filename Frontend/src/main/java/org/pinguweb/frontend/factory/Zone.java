package org.pinguweb.frontend.factory;

import lombok.Getter;
import lombok.Setter;
import org.pinguweb.DTO.DTO;
import org.pinguweb.DTO.NeedDTO;
import org.pinguweb.DTO.ZoneDTO;
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

import java.util.List;

@Setter
@Getter
public class Zone extends MapObject{

    List<LLatLng> points;

    LPolygon polygon;

    public Zone(Double latitude, Double longitude){
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public void addPoint(LComponentManagementRegistry reg, Tuple<Double, Double> coord){
        this.points.add(new LLatLng(reg, coord._1(), coord._2()));
    }

    public LPolygon generatePolygon(LComponentManagementRegistry reg, String lineColor, String fillColor){
        this.polygon = new LPolygon(reg, points, new LPolylineOptions().withColor(lineColor).withFillColor(fillColor));
        return this.polygon;
    }

    public void addToMap(LMap map){
        this.getPolygon().addTo(map);
    }

    @Override
    public void pushToServer(String url, DTO dto){
        ZoneDTO zoneDTO = (ZoneDTO) dto;
        String finurl = "/api/zone/" + zoneDTO.getId();
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
    public void deleteFromServer(String url, DTO dto) {
        ZoneDTO zoneDTO = (ZoneDTO) dto;
        String finurl = "/api/zone/" + zoneDTO.getId();
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
