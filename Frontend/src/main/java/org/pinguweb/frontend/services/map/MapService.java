package org.pinguweb.frontend.services.map;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import lombok.Getter;
import lombok.Setter;
import org.pinguweb.DTO.NeedDTO;
import org.pinguweb.DTO.ZoneDTO;
import org.pinguweb.frontend.factory.Marker;
import org.pinguweb.frontend.factory.MarkerFactory;
import org.pinguweb.frontend.factory.Zone;
import org.pinguweb.frontend.factory.ZoneFactory;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.pinguweb.frontend.view.MapView;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.basictypes.LIcon;
import software.xdev.vaadin.maps.leaflet.basictypes.LIconOptions;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.basictypes.LPoint;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarkerOptions;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolygon;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolylineOptions;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.util.ArrayList;
import java.util.List;

@Setter
@Service
public class MapService {
    @Setter
    private LComponentManagementRegistry reg;

    private int tempIdNeed = 0;
    private int tempIdZone = 0;

    @Setter
    private LMap map;

    @Setter
    @Getter
    private boolean zoneBool = false;

    @Setter
    @Getter
    private boolean deleteBool = false;

    @Setter
    private String ID;

    private String clickFuncReference;

    @Getter
    private ArrayList<LMarker> markers = new ArrayList<>();

    @Getter
    private ArrayList<LPolygon> polygons = new ArrayList<>();

    @Getter
    private NeedDTO need;

    @Getter
    private ZoneDTO zone;

    private MarkerFactory markerFactory;
    private ZoneFactory zoneFactory;

    public MapService() {
        this.markerFactory = new MarkerFactory();
        this.zoneFactory = new ZoneFactory();
    }


    @Async
    public void load() {
        BackendObject<List<NeedDTO>> needs = BackendService.getListFromBackend(BackendService.BACKEND + "/api/need",
                new ParameterizedTypeReference<>() {});

        if (needs.getStatusCode() == HttpStatus.OK) {
            for (NeedDTO need : needs.getData()) {
                createNeed(need.getLatitude(), need.getLongitude());
            }
        }

        BackendObject<List<ZoneDTO>> zonas = BackendService.getListFromBackend(BackendService.BACKEND + "/api/zone",
                new ParameterizedTypeReference<>() {});

        if (zonas.getStatusCode() == HttpStatus.OK) {
            for (ZoneDTO zone : zonas.getData()) {
                List<Tuple<Double, Double>> points = new ArrayList<>();

                for(int i = 0; i < zone.getLatitudes().size(); i++){
                    points.add(new Tuple<>(zone.getLatitudes().get(i), zone.getLongitudes().get(i)));
                }

                createZone(points);
            }
        }
    }

    // TODO: Texto para el el marcador de tarea
    public void createNeed(double lat, double lng) {
        Marker marker = (Marker) markerFactory.createMapObject(reg, lat, lng);
        marker = marker.convertToZoneMarker(reg);
        marker.addToMap(this.map);

        markers.add(marker.getMarkerObj());
        MapView.getLLayerGroupNeeds().addLayer(marker.getMarkerObj());
        this.map.addLayer(MapView.getLLayerGroupNeeds());
    }

    public LMarker createZoneMarker(double lat, double lng) {
        Marker marker = (Marker) markerFactory.createMapObject(reg, lat, lng);
        marker = marker.convertToZoneMarker(reg);

        marker.getMarkerObj().on("dragstart", "e => document.getElementById('" + ID + "').$server.zoneMarkerStart(e.target.getLatLng())");
        marker.getMarkerObj().on("dragend", "e => document.getElementById('" + ID + "').$server.zoneMarkerEnd(e.target.getLatLng())");
        marker.addToMap(this.map);

        return marker.getMarkerObj();
    }

    public void createZone(List<Tuple<Double, Double>> markers) {
        Tuple<Double, Double> first = markers.getFirst();
        Zone zone = (Zone) zoneFactory.createMapObject(this.reg, first._1(), first._2());

        for (Tuple<Double, Double> marker : markers) {
            zone.addPoint(this.reg, marker);
        }

        LPolygon polygon = zone.generatePolygon(this.reg, "red", "blue");
        clickFuncReference = map.clientComponentJsAccessor() + ".myCoolClickFunc";
        reg.execJs(clickFuncReference + "=e => document.getElementById('" + ID + "').$server.clickOnZone(e.latlng)");

        polygon.on("click", clickFuncReference);

        zone.addToMap(map);
        // TODO: GESTIONAR AQUÍ ZONA EN VEZ DE POLYGON
        polygons.add(polygon);
        MapView.getLLayerGroupZones().addLayer(polygon);
        this.map.addLayer(MapView.getLLayerGroupZones());
    }

    public void setZone(String description, String mame, String severity) {
        this.zone = new ZoneDTO();
        this.zone.setId(tempIdZone);
        this.zone.setDescription(description);
        this.zone.setName(mame);
        //TODO: asignar latitudes y longitudes y cambiar
        this.zone.setLatitudes(new ArrayList<>());
        this.zone.setLongitudes(new ArrayList<>());
        this.zone.setCatastrophe(null);
        this.zone.setEmergencyLevel(severity);
        this.zone.setStorages(new ArrayList<>());

        this.tempIdZone++;

    }

    public void setNeed(String description, String type, String affected, String urgency, int task, int catastrophe) {
        this.need = new NeedDTO();
        this.need.setId(tempIdNeed);
        this.need.setDescription(description);
        this.need.setNeedType(type);
        this.need.setAffected(affected);
        this.need.setLatitude(0.0);
        this.need.setLongitude(0.0);
        this.need.setCatastrophe(catastrophe);
        this.need.setUrgency(urgency);
        this.need.setTask(task);

        this.tempIdNeed++;
    }

}
