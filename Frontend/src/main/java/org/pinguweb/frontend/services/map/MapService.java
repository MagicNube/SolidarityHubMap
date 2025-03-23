package org.pinguweb.frontend.services.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
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
    private LLatLng coords;
    private final ArrayList<LLatLng> points = new ArrayList<>();

    @Setter
    @Getter
    private boolean zone = false;

    public MapService() {
    }

    // TODO: Texto para el el marcador de tarea
    public void createTask(){
        // TODO: ¿Modificar el mapa? realmente no tengo ni idea
        // TODO: ¿Cambiar cursor?
        System.out.println("Task created at: " + coords);
        LMarker marker = new LMarker(reg,coords);
        marker.addTo(map);
        UI.getCurrent();
    }

    public void createZone(){
        // TODO: ¿Modificar el mapa? realmente no tengo ni idea
        // TODO: ¿Cambiar cursor?
        new LPolygon(reg, points.toArray(new LLatLng[0])).addTo(map);
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

    public void setCoords(double lat, double lng) {
        this.coords = new LLatLng(reg, lat, lng);
    }

    public void addPoint(double lat, double lng){
        points.add(new LLatLng(reg, lat, lng));
    }

    public int getNumPoints(){
        return points.size();
    }
}
