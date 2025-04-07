package org.pinguweb.frontend.services.map;


import lombok.Getter;
import lombok.Setter;
import org.pinguweb.frontend.factory.Marker;
import org.pinguweb.frontend.factory.MarkerFactory;
import org.pinguweb.frontend.factory.Zone;
import org.pinguweb.frontend.factory.ZoneFactory;
import org.pinguweb.DTO.NeedDTO;
import org.pinguweb.DTO.ZoneDTO;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.pinguweb.frontend.view.MapView;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.basictypes.LLatLng;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolygon;
import software.xdev.vaadin.maps.leaflet.layer.vector.LPolylineOptions;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Setter
@Service
public class MapService {
    @Setter
    @Getter
    int tempIdZone = 0;

    @Setter
    @Getter
    int tempIdMarker = 0;


    @Setter
    private LComponentManagementRegistry reg;

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

    @Getter
    private HashSet<Marker> markers = new HashSet<>();

    @Getter
    private HashSet<Zone> zones = new HashSet<>();

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
    /*public void load() {
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
    }*/

    // TODO: Texto para el el marcador de tarea
    public void createNeed(double lat, double lng, NeedDTO needDTO) {
        Marker marker = (Marker) markerFactory.createMapObject(reg, lat, lng);
        marker = marker.convertToZoneMarker(reg);
        marker.setNeedDTO(needDTO);
        marker.addToMap(this.map);

        markers.add(marker);
        MapView.getLLayerGroupNeeds().addLayer(marker.getMarkerObj());
        this.map.addLayer(MapView.getLLayerGroupNeeds());

        for (Marker m : markers) {
            System.out.println("ID: " + m.getNeedDTO().getId() + m );
        }
        System.out.println("Fin");
    }

    public void deleteNeed(int id) {
        Marker marker = markers.stream()
                .filter(m -> m.getNeedDTO().getId() == id)
                .findFirst()
                .orElse(null);
        if (marker != null) {
            marker.removeFromMap(this.map);
            markers.remove(marker);
            MapView.getLLayerGroupNeeds().removeLayer(marker.getMarkerObj());
            this.map.addLayer(MapView.getLLayerGroupNeeds());
        }
    }



    public Marker createZoneMarker(double lat, double lng) {
        Marker marker = (Marker) markerFactory.createMapObject(reg, lat, lng);
        marker = marker.convertToZoneMarker(reg);

        marker.getMarkerObj().on("dragstart", "e => document.getElementById('" + ID + "').$server.zoneMarkerStart(e.target.getLatLng())");
        marker.getMarkerObj().on("dragend", "e => document.getElementById('" + ID + "').$server.zoneMarkerEnd(e.target.getLatLng())");
        marker.addToMap(this.map);

        return marker;
    }


    public void createZone(List<Tuple<Double, Double>> markers, ZoneDTO zoneDTO) {
        Zone zone = (Zone) zoneFactory.createMapObject(reg, 0.0, zoneDTO.getId()+0.0);
        for (Tuple<Double, Double> marker : markers) {
            zone.addPoint(reg, marker);
        }
        zone.generatePolygon(reg, "red", "blue");
        zone.setZoneDTO(zoneDTO);
        zone.addToMap(this.map);
        zone.getPolygon().on("click", "e => document.getElementById('" + ID + "').$server.clickOnZone(e.latlng)");

        zones.add(zone);
        MapView.getLLayerGroupZones().addLayer(zone.getPolygon());
        this.map.addLayer(MapView.getLLayerGroupZones());
    }

    public void deleteZone(int id) {
        Zone zone = zones.stream()
                .filter(z -> z.getZoneDTO().getId() == id)
                .findFirst()
                .orElse(null);
        if (zone != null) {
            zone.removeFromMap(this.map);
            zones.remove(zone);
            MapView.getLLayerGroupZones().removeLayer(zone.getPolygon());
            this.map.addLayer(MapView.getLLayerGroupZones());
        }
    }

}
