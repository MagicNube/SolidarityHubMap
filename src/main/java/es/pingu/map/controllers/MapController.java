package es.pingu.map.controllers;

import com.vaadin.flow.component.UI;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolygon;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.util.ArrayList;

public class MapController {
    private final LComponentManagementRegistry reg;
    private final LMap map;
    private LLatLng coords;
    private final ArrayList<LLatLng> points = new ArrayList<>();

    @Setter
    @Getter
    private boolean zone = false;




    public MapController(@NotNull LComponentManagementRegistry reg, @NonNull LMap map) {
        this.reg = reg;
        this.map = map;
    }


    // TODO: Texto para el el marcador de tarea
    public void createTask(){
        // TODO: ¿Modificar el mapa? realmente no tengo ni idea
        // TODO: ¿Cambiar cursor?
        System.out.println("Task created at: " + coords);
        new LMarker(reg,coords).addTo(map);
        UI.getCurrent();

    }

    public void createZone(){
        // TODO: ¿Modificar el mapa? realmente no tengo ni idea
        // TODO: ¿Cambiar cursor?
        new LPolygon(reg, points.toArray(new LLatLng[0])).addTo(map);
        points.clear();
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