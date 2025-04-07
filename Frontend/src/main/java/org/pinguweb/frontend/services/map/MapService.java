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


    public MapService() {}


    @Async
    public void load() {
        BackendObject<List<NeedDTO>> needs = BackendService.getListFromBackend(BackendService.BACKEND + "/api/need",
                new ParameterizedTypeReference<List<NeedDTO>>() {});

        if (needs.getStatusCode() == HttpStatus.OK) {
            for (NeedDTO need : needs.getData()) {
                createNeed(need.getLatitude(), need.getLongitude());
            }
        }
    }

    // TODO: Texto para el el marcador de tarea
    public void createNeed(double lat, double lng) {
        LLatLng coords = new LLatLng(this.reg, lat, lng);

        LMarker marker = new LMarker(this.reg, coords);
        marker.addTo(this.map);

        markers.add(marker);
        MapView.getLLayerGroupNeeds().addLayer(marker);
        this.map.addLayer(MapView.getLLayerGroupNeeds());
    }

    public void createZone(List<Tuple<Double, Double>> markers) {

        List<LLatLng> points = new ArrayList<>();

        for (Tuple<Double, Double> marker : markers) {
            points.add(new LLatLng(this.reg, marker._1(), marker._2()));
        }

        LPolygon polygon = new LPolygon(this.reg, points, new LPolylineOptions().withColor("red").withFillColor("blue"));

        clickFuncReference = map.clientComponentJsAccessor() + ".myCoolClickFunc";
        reg.execJs(clickFuncReference + "=e => document.getElementById('" + ID + "').$server.clickOnZone(e.latlng)");

        polygon.on("click", clickFuncReference);

        polygon.addTo(this.map);

        polygons.add(polygon);
        MapView.getLLayerGroupZones().addLayer(polygon);
        this.map.addLayer(MapView.getLLayerGroupZones());

        points.clear();
    }

    public LMarker createZoneMarker(double lat, double lng) {
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

    public void deleteZone(ZoneDTO dto){
        String url = "/api/zone/" + dto.getId();
        try{
            HttpStatusCode status = BackendService.deleteFromBackend(url);
            if (status == HttpStatus.OK){
                //TODO: Eliminar del mapa
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteNeed(NeedDTO dto){
        String url = "/api/need/" + dto.getId();
        try{
            HttpStatusCode status = BackendService.deleteFromBackend(url);
            if (status == HttpStatus.OK){
                //TODO: Eliminar del mapa
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
