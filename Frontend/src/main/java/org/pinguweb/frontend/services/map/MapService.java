package org.pinguweb.frontend.services.map;


import lombok.Getter;
import lombok.Setter;
import org.pinguweb.DTO.NeedDTO;
import org.pinguweb.DTO.RouteDTO;
import org.pinguweb.DTO.ZoneDTO;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.mapObjects.factories.*;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.pinguweb.frontend.view.MapView;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.Tuple;
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
    @Getter
    int tempIdRoute = 0;

    @Setter
    @Getter
    int tempIdRoutePoint = 0;

    @Setter
    @Getter
    private Boolean pointInZone = false;

    @Setter
    @Getter
    private boolean creatingNeed = false;

    @Setter
    @Getter
    private boolean creatingRoute = false;

    @Setter
    @Getter
    private boolean editing = false;

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
    private HashSet<Need> needs = new HashSet<>();

    @Getter
    private HashSet<Zone> zones = new HashSet<>();

    @Getter
    private HashSet<Route> routes = new HashSet<>();

    @Getter
    private HashMap<Integer,List<RoutePoint>> routePoints = new HashMap<>();


    private NeedFactory needFactory;
    private ZoneFactory zoneFactory;
    private ZoneMarkerFactory zoneMarkerFactory;
    private RouteFactory routeFactory;
    private RoutePointFactory routePointFactory;

    public MapService() {
        this.needFactory = new NeedFactory();
        this.zoneFactory = new ZoneFactory();
        this.zoneMarkerFactory = new ZoneMarkerFactory();
        this.routeFactory = new RouteFactory();
        this.routePointFactory = new RoutePointFactory();
        load();
    }


    @Async
    public void load() {
        BackendObject<List<NeedDTO>> needs = BackendService.getListFromBackend(BackendService.BACKEND + "/api/need",
                new ParameterizedTypeReference<>() {});

        if (needs.getStatusCode() == HttpStatus.OK) {
            for (NeedDTO need : needs.getData()) {
                createNeed(need);
            }
        }

        BackendObject<List<ZoneDTO>> zonas = BackendService.getListFromBackend(BackendService.BACKEND + "/api/zone",
                new ParameterizedTypeReference<>() {});

        if (zonas.getStatusCode() == HttpStatus.OK) {
            for (ZoneDTO zone : zonas.getData()) {
                createZone(zone);
            }
        }
    }

    // TODO: Texto para el el marcador de tarea
    public Need createNeed(NeedDTO needDTO) {
        double lat = needDTO.getLatitude();
        double lng = needDTO.getLongitude();
        Need need = (Need) needFactory.createMapObject(reg, lat, lng);
        need.setID(needDTO.getID());
        need.addToMap(this.map);
        need.getMarkerObj().on("click", "e => document.getElementById('" + ID + "').$server.clickOnNeed(e.latlng, " + need.getID() + ")");

        needs.add(need);
        MapView.getLLayerGroupNeeds().addLayer(need.getMarkerObj());
        this.map.addLayer(MapView.getLLayerGroupNeeds());

        for (Need m : needs) {
            System.out.println("ID: " + m.getID() + m );
        }
        System.out.println("Fin");

        return need;
    }

    public void deleteNeed(int ID) {
        Need need = needs.stream()
                .filter(m -> m.getID() == ID)
                .findFirst()
                .orElse(null);
        if (need != null) {
            need.removeFromMap(this.map);
            need.deleteFromServer();
            needs.remove(need);
            MapView.getLLayerGroupNeeds().removeLayer(need.getMarkerObj());
            this.map.addLayer(MapView.getLLayerGroupNeeds());
        }
    }

    public ZoneMarker createZoneMarker(double lat, double lng) {
        ZoneMarker zoneMarker = (ZoneMarker) zoneMarkerFactory.createMapObject(reg, lat, lng);

        zoneMarker.getMarkerObj().on("dragstart", "e => document.getElementById('" + ID + "').$server.zoneMarkerStart(e.target.getLatLng())");
        zoneMarker.getMarkerObj().on("dragend", "e => document.getElementById('" + ID + "').$server.zoneMarkerEnd(e.target.getLatLng())");
        zoneMarker.addToMap(this.map);

        return zoneMarker;
    }

    public RoutePoint createRoutePoint(double lat, double lng) {
        RoutePoint routePoint = (RoutePoint) routePointFactory.createMapObject(reg, lat, lng);

        routePoint.getMarkerObj().on("dragstart", "e => document.getElementById('" + ID + "').$server.routePointStart(e.target.getLatLng())");
        routePoint.getMarkerObj().on("dragend", "e => document.getElementById('" + ID + "').$server.routePointEnd(e.target.getLatLng())");
        routePoint.addToMap(this.map);

        return routePoint;
    }

    public Zone createZone(ZoneDTO zoneDTO) {
        List<Tuple<Double, Double>> points = new ArrayList<>();

        for(int i = 0; i < zoneDTO.getLatitudes().size(); i++){
            points.add(new Tuple<>(zoneDTO.getLatitudes().get(i), zoneDTO.getLongitudes().get(i)));
        }

        Zone zone = (Zone) zoneFactory.createMapObject(reg, 0.0, zoneDTO.getID()+0.0);
        for (Tuple<Double, Double> marker : points) {
            zone.addPoint(reg, marker);
        }

        zone.generatePolygon(reg, "red", "blue");
        zone.setID(zoneDTO.getID());
        zone.addToMap(this.map);
        zone.getPolygon().on("click", "e => document.getElementById('" + ID + "').$server.clickOnZone(e.latlng, " + zone.getID() + ")");

        zones.add(zone);
        MapView.getLLayerGroupZones().addLayer(zone.getPolygon());
        this.map.addLayer(MapView.getLLayerGroupZones());

        return zone;
    }

    public void deleteZone(int ID) {
        Zone zone = zones.stream()
                .filter(z -> z.getID() == ID)
                .findFirst()
                .orElse(null);
        if (zone != null) {
            zone.removeFromMap(this.map);
            zone.deleteFromServer();
            zones.remove(zone);
            MapView.getLLayerGroupZones().removeLayer(zone.getPolygon());
            this.map.addLayer(MapView.getLLayerGroupZones());
        }
    }

    public Route createRoute(RouteDTO routeDTO,List<RoutePoint> routePoints) {
        Route route = (Route) routeFactory.createMapObject(reg, 0.0, routeDTO.getID() + 0.0);
        route.setID(routeDTO.getID());
        for (RoutePoint routePoint : routePoints) {
            route.addPoint(reg, new Tuple<>(routePoint.getLatitude(), routePoint.getLongitude()));
        }
        route.generatePolygon(reg, "red", "blue");
        route.setID(routeDTO.getID());
        route.addToMap(this.map);

        //route.getPolygon().on("click", "e => document.getElementById('" + ID + "').$server.clickOnRoute(e.latlng, " + route.getID() + ")");
        routes.add(route);


        return route;
    }

    public void deleteRoute(int ID) {
        Route route = routes.stream()
                .filter(r -> r.getID() == ID)
                .findFirst()
                .orElse(null);
        if (route != null) {
            route.removeFromMap(this.map);
            route.deleteFromServer();
            routes.remove(route);
            List<RoutePoint> routePoints = this.routePoints.get(route.getID());
            if (routePoints != null) {
                for (RoutePoint routePoint : routePoints) {
                    routePoint.removeFromMap(this.map);
                    routePoint.deleteFromServer();
                }
                this.routePoints.remove(route.getID());
            }
            System.out.println("Route deleted: " + route.getID());
        }
    }

}
