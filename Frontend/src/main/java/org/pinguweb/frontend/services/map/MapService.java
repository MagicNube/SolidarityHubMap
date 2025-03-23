package org.pinguweb.frontend.services.map;

import com.vaadin.flow.component.UI;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.stereotype.Service;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolygon;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.io.Serializable;
import java.util.ArrayList;

@Service
public class MapService {
    @Setter
    private LComponentManagementRegistry reg;

    @Setter
    private LMap map;
    private final ArrayList<LLatLng> points = new ArrayList<>();

    @Setter
    @Getter
    private boolean zone = false;

    public MapService() {
    }

    // TODO: Texto para el el marcador de tarea
    public void createTask(double lat, double lng) {
        // TODO: ¿Modificar el mapa? realmente no tengo ni idea

        LLatLng coords = new LLatLng(this.reg, lat, lng);

        new LMarker(reg,coords).addTo(map);
        UI.getCurrent();
    }

    public void createZone(){
        // TODO: ¿Modificar el mapa? realmente no tengo ni idea
        new LPolygon(reg, points.toArray(new LLatLng[0])).addTo(map);
        points.clear();
    }

    public void addPoint(double lat, double lng){
        points.add(new LLatLng(reg, lat, lng));
    }

    public int getNumPoints(){
        return points.size();
    }
}
