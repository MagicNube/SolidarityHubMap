package org.pinguweb.frontend.services.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.basictypes.LIcon;
import software.xdev.vaadin.maps.leaflet.basictypes.LIconOptions;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.basictypes.LPoint;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarkerOptions;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolygon;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {
    @Setter
    private LComponentManagementRegistry reg;

    @Setter
    private LMap map;

    @Setter
    @Getter
    private boolean zone = false;

    @Setter
    private String ID;

    public MapService() {
    }

    // TODO: Texto para el el marcador de tarea
    public void createTask(double lat, double lng) {
        LLatLng coords = new LLatLng(this.reg, lat, lng);

        new LMarker(reg,coords).addTo(map);
        UI.getCurrent();
    }

    public void createZone(List<Tuple<Double, Double>> markers){

        List<LLatLng> points = new ArrayList<>();

        for (Tuple<Double, Double> marker : markers) {
            points.add(new LLatLng(this.reg, marker._1(), marker._2()));
        }

        new LPolygon(reg, points).addTo(map);
        points.clear();
    }

    public void getAllPointsSaved(){
        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject("http://localhost:8081/api/zone/1", String.class);

        // TODO: Ejemplo de una petición al servidor REST

        /*try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResponse, new TypeReference<List<Task>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public LMarker createZoneMarker(double lat, double lng){
        LIcon icon = new LIcon(this.reg, new LIconOptions()
                .withIconUrl("https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png")
                .withShadowUrl("https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png")
                .withIconSize(new LPoint(this.reg, 25, 41))
                .withIconAnchor(new LPoint(this.reg, 12, 41))
                .withPopupAnchor(new LPoint(this.reg, 1, -34))
                .withShadowSize(new LPoint(this.reg, 41, 41))
        );

        LMarkerOptions options = new LMarkerOptions().withDraggable(true).withIcon(icon);

        LMarker marker = new LMarker(this.reg, new LLatLng(this.reg, lat, lng), options);

        marker.on("dragstart", "e => document.getElementById('" + ID + "').$server.zoneMarkerStart(e.target.getLatLng())");
        marker.on("dragend", "e => document.getElementById('" + ID + "').$server.zoneMarkerEnd(e.target.getLatLng())");

        marker.addTo(this.map);
        return marker;
    }
}
