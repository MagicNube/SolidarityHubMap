package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.*;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.mapObjects.factories.*;
import org.pinguweb.frontend.services.backend.BackendObject;
import org.pinguweb.frontend.services.backend.BackendService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Setter
@Getter
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
    private LComponentManagementRegistry reg;

    @Setter
    private LMap map;

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

    private ZoneDTO tempZoneDTO;
    private RouteDTO tempRouteDTO;

    private HashMap<Tuple<Double, Double>, ZoneMarker> zoneMarkers = new HashMap<>();
    private List<Tuple<Double, Double>> zoneMarkerPoints = new ArrayList<>();
    private Tuple<Double, Double> zoneMarkerStartingPoint;

    private Tuple<Double, Double> routePointStartingPoint;
    private List<RoutePoint> routePoint = new ArrayList<>();

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
    }

    @Async
    public void load() {
            for (NeedDTO need : Need.getAllFromServer()) {
                log.debug(need.toString());
                if (need.getLatitude() != null && need.getLongitude() != null) {
                    createNeed(need);
                }
            }

            for (ZoneDTO zone : Zone.getAllFromServer()) {
                if (!zone.getLatitudes().isEmpty() && !zone.getLongitudes().isEmpty()) {
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

        needs.add(need);
        //MapView.getLLayerGroupNeeds().addLayer(need.getMarkerObj());
        //this.map.addLayer(MapView.getLLayerGroupNeeds());

        for (Need m : needs) {
            log.debug("ID: " + m.getID() + m );
        }
        log.debug("Fin");

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
            //MapView.getLLayerGroupNeeds().removeLayer(need.getMarkerObj());
            //this.map.addLayer(MapView.getLLayerGroupNeeds());
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

        Zone zone = (Zone) zoneFactory.createMapObject(reg, 0.0, zoneDTO.getID()+1.0);
        for (Tuple<Double, Double> marker : points) {
            zone.addPoint(reg, marker);
        }

        zone.setName(zoneDTO.getName() != null ? zoneDTO.getName() : "Sin nombre");
        zone.setDescription(zoneDTO.getDescription() != null ? zoneDTO.getDescription() : "Sin descripción");
        zone.setEmergencyLevel(zoneDTO.getEmergencyLevel() != null ? zoneDTO.getEmergencyLevel() : "Bajo"); // Nivel por defecto: "Bajo"
        zone.setCatastrophe(zoneDTO.getCatastrophe() != null ? zoneDTO.getCatastrophe() : -1); // -1 podría indicar "sin catástrofe"
        zone.setStorages(zoneDTO.getStorages() != null ? zoneDTO.getStorages() : new ArrayList<>());
        zone.setLatitudes(zoneDTO.getLatitudes() != null ? zoneDTO.getLatitudes() : List.of(0.0));
        zone.setLongitudes(zoneDTO.getLongitudes() != null ? zoneDTO.getLongitudes() : List.of(0.0));

        zone.generatePolygon(reg, "red", "blue");
        zone.setID(zoneDTO.getID());
        zone.addToMap(this.map);

        zones.add(zone);
        //MapView.getLLayerGroupZones().addLayer(zone.getPolygon());
        //this.map.addLayer(MapView.getLLayerGroupZones());

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
            //MapView.getLLayerGroupZones().removeLayer(zone.getPolygon());
            //this.map.addLayer(MapView.getLLayerGroupZones());
        }
    }

    public Route createRoute(RouteDTO routeDTO, List<RoutePoint> routePoints) {
        Route route = (Route) routeFactory.createMapObject(reg, 0.0, routeDTO.getID() + 0.0);
        route.setID(routeDTO.getID());
        for (RoutePoint routePoint : routePoints) {
            route.addPoint(reg, new Tuple<>(routePoint.getLatitude(), routePoint.getLongitude()));
        }
        route.generatePolygon(reg, "red", "blue");

        route.setID(routeDTO.getID());
        route.setRouteType(routeDTO.getRouteType());
        route.setName(routeDTO.getName() != null ? routeDTO.getName() : "Sin nombre");
        route.setCatastrophe(routeDTO.getCatastrophe() != null ? routeDTO.getCatastrophe() : -1); // -1 podría indicar "sin catástrofe"

        route.addToMap(this.map);

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
            log.debug("Route deleted: {}", route.getID());
        }
    }

    public Need getNeedByID(String ID) {
        return needs.stream()
                .filter(m -> m.getID() == Integer.parseInt(ID))
                .findFirst()
                .orElse(null);
    }

    public Zone getZoneByID(String ID) {
        return zones.stream()
                .filter(z -> z.getID() == Integer.parseInt(ID))
                .findFirst()
                .orElse(null);
    }

    public Route getRouteByID(String ID) {
        return routes.stream()
                .filter(r -> r.getID() == Integer.parseInt(ID))
                .findFirst()
                .orElse(null);
    }


}
