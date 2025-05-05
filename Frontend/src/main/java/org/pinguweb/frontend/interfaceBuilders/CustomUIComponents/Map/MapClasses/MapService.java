package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapClasses;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.NeedDTO;
import org.pingu.domain.DTO.RouteDTO;
import org.pingu.domain.DTO.RoutePointDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.mapObjects.factories.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.layer.LLayerGroup;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.map.LMapLocateOptions;
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
    private HashMap<Integer, List<RoutePoint>> routePoints = new HashMap<>();

    private LLayerGroup lLayerGroupZones;
    private LLayerGroup lLayerGroupNeeds;
    private LLayerGroup lLayerGroupRoutes;
    private LLayerGroup lLayerGroupStorages;

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

        for (RouteDTO route : Route.getAllFromServer()) {
            if (!route.getPoints().isEmpty()) {
                List<RoutePoint> routePoints = new ArrayList<>();
                for (int i = 0; i < route.getPoints().size(); i++) {
                    RoutePointDTO routePoint =  RoutePoint.getFromServerById(route.getPoints().get(i));
                    if (routePoint != null) {
                        RoutePoint point = (RoutePoint) routePointFactory.createMapObject(reg, routePoint.getLatitude(), routePoint.getLongitude());
                        point.setID(routePoint.getID());
                        point.getMarkerObj().bindPopup(route.getName());
                        routePoints.add(point);
                        this.routePoints.computeIfAbsent(route.getID(), k -> new ArrayList<>()).add(point);
                        if (i == 0 || i == route.getPoints().size() - 1) {
                            point.addToMap(this.map);
                        }
                    } else {
                        log.debug("RoutePoint not found: " + route.getPoints().get(i));
                    }
                }
                if (!routePoints.isEmpty()) {
                    createRoute(route, routePoints);
                }
            }
        }
    }

    public Need createNeed(NeedDTO needDTO) {
        double lat = needDTO.getLatitude();
        double lng = needDTO.getLongitude();
        Need need = (Need) needFactory.createMapObject(reg, lat, lng);
        need.setID(needDTO.getID());
        need.addToMap(this.map);

        need.getMarkerObj().bindPopup(needDTO.getNeedType());
        needs.add(need);
        lLayerGroupNeeds.addLayer(need.getMarkerObj());
        this.map.addLayer(lLayerGroupNeeds);

        for (Need m : needs) {
            log.debug("ID: " + m.getID() + m);
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
            lLayerGroupNeeds.removeLayer(need.getMarkerObj());
            this.map.addLayer(lLayerGroupNeeds);
        }
    }

    public ZoneMarker createZoneMarker(double lat, double lng) {
        ZoneMarker zoneMarker = (ZoneMarker) zoneMarkerFactory.createMapObject(reg, lat, lng);

        String clickFuncReferenceDragStart = this.map.clientComponentJsAccessor() + ".myClickFuncDragStart"+zoneMarker.getID();
        String clickFuncReferenceDragEnd = this.map.clientComponentJsAccessor() + ".myClickFuncDragEnd"+zoneMarker.getID();

        reg.execJs(clickFuncReferenceDragStart + "=e => document.getElementById('" + ID + "').$server.routePointStart(e.target.getLatLng())");
        reg.execJs(clickFuncReferenceDragEnd + "=e => document.getElementById('" + ID + "').$server.routePointEnd(e.target.getLatLng())");

        zoneMarker.getMarkerObj().on("dragstart", clickFuncReferenceDragStart);
        zoneMarker.getMarkerObj().on("dragend", clickFuncReferenceDragEnd);

        zoneMarker.addToMap(this.map);

        return zoneMarker;
    }

    public RoutePoint createRoutePoint(double lat, double lng) {
        RoutePoint routePoint = (RoutePoint) routePointFactory.createMapObject(reg, lat, lng);

        String clickFuncReferenceDragStart = this.map.clientComponentJsAccessor() + ".myClickFuncDragStart"+routePoint.getID();
        String clickFuncReferenceDragEnd = this.map.clientComponentJsAccessor() + ".myClickFuncDragEnd"+routePoint.getID();

        reg.execJs(clickFuncReferenceDragStart + "=e => document.getElementById('" + ID + "').$server.routePointStart(e.target.getLatLng())");
        reg.execJs(clickFuncReferenceDragEnd + "=e => document.getElementById('" + ID + "').$server.routePointEnd(e.target.getLatLng())");

        routePoint.getMarkerObj().on("dragstart", clickFuncReferenceDragStart);
        routePoint.getMarkerObj().on("dragend", clickFuncReferenceDragEnd);

        routePoint.addToMap(this.map);

        return routePoint;
    }

    public Zone createZone(ZoneDTO zoneDTO) {
        List<Tuple<Double, Double>> points = new ArrayList<>();

        for (int i = 0; i < zoneDTO.getLatitudes().size(); i++) {
            points.add(new Tuple<>(zoneDTO.getLatitudes().get(i), zoneDTO.getLongitudes().get(i)));
        }

        Zone zone = (Zone) zoneFactory.createMapObject(reg, 0.0, zoneDTO.getID() + 1.0);
        for (Tuple<Double, Double> marker : points) {
            zone.addPoint(reg, marker);
        }

        zone.setName(zoneDTO.getName() != null ? zoneDTO.getName() : "Sin nombre");
        zone.setDescription(zoneDTO.getDescription() != null ? zoneDTO.getDescription() : "Sin descripción");
        zone.setEmergencyLevel(zoneDTO.getEmergencyLevel() != null ? zoneDTO.getEmergencyLevel() : "LOW"); // Nivel por defecto: "Bajo"
        zone.setCatastrophe(zoneDTO.getCatastrophe() != null ? zoneDTO.getCatastrophe() : -1); // -1 podría indicar "sin catástrofe"
        zone.setStorages(zoneDTO.getStorages() != null ? zoneDTO.getStorages() : new ArrayList<>());
        zone.setLatitudes(zoneDTO.getLatitudes() != null ? zoneDTO.getLatitudes() : List.of(0.0));
        zone.setLongitudes(zoneDTO.getLongitudes() != null ? zoneDTO.getLongitudes() : List.of(0.0));

        switch (zoneDTO.getEmergencyLevel()) {
            case "LOW":
                zone.generatePolygon(reg, "grey", "green");
                break;
            case "MEDIUM":
                zone.generatePolygon(reg, "grey", "yellow");
                break;
            case "HIGH":
                zone.generatePolygon(reg, "grey", "red");
                break;
            case "VERYHIGH":
                zone.generatePolygon(reg, "grey", "black");
                break;
            default:
                zone.generatePolygon(reg, "grey", "blue");
        }
        zone.getPolygon().bindPopup(zone.getName());
        zone.setID(zoneDTO.getID());
        zone.addToMap(this.map);

        zones.add(zone);
        lLayerGroupZones.addLayer(zone.getPolygon());
        this.map.addLayer(lLayerGroupZones);

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
            lLayerGroupZones.removeLayer(zone.getPolygon());
            this.map.addLayer(lLayerGroupZones);
        }
    }

    public Route createRoute(RouteDTO routeDTO, List<RoutePoint> routePoints) {
        Route route = (Route) routeFactory.createMapObject(reg, 0.0, routeDTO.getID() + 1.0);
        route.setID(routeDTO.getID());
        for (RoutePoint routePoint : routePoints) {
            route.addPoint(reg, new Tuple<>(routePoint.getLatitude(), routePoint.getLongitude()));
        }

        switch (routeDTO.getRouteType()) {
            case "PREFERRED_ROUTE":
                route.generatePolygon(reg, "green", "green");
                break;
            case "LOW_RISK":
                route.generatePolygon(reg, "yellow", "yellow");
                break;
            case "MEDIUM_RISK":
                route.generatePolygon(reg, "red", "red");
                break;
            case "HIGH_RISK":
                route.generatePolygon(reg, "black", "black");
                break;
            case "CLOSED":
                route.generatePolygon(reg, "blue", "blue");
                break;
            default:
                route.generatePolygon(reg, "blue", "blue");
        }

        route.setID(routeDTO.getID());
        route.setRouteType(routeDTO.getRouteType());
        route.setName(routeDTO.getName() != null ? routeDTO.getName() : "Sin nombre");
        route.setCatastrophe(routeDTO.getCatastrophe() != null ? routeDTO.getCatastrophe() : -1); // -1 podría indicar "sin catástrofe"
        route.setPointsID(routeDTO.getPoints() != null ? (ArrayList<Integer>) routeDTO.getPoints() : new ArrayList<>());

        route.getPolygon().bindPopup(route.getName());
        routes.add(route);

        lLayerGroupRoutes.addLayer(route.getPolygon());
        lLayerGroupRoutes.addLayer(routePoints.get(0).getMarkerObj());
        lLayerGroupRoutes.addLayer(routePoints.get(routePoints.size() - 1).getMarkerObj());
        this.map.addLayer(lLayerGroupRoutes);

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
            List<RoutePoint> routePoints = this.routePoints.get(route.getID());
            if (routePoints != null) {
                for (RoutePoint routePoint : routePoints) {
                    routePoint.deleteFromServer();
                    routePoint.removeFromMap(this.map);
                }
                routes.remove(route);
                lLayerGroupRoutes.removeLayer(route.getPolygon());
                lLayerGroupRoutes.addLayer(routePoints.get(0).getMarkerObj());
                lLayerGroupRoutes.addLayer(routePoints.get(routePoints.size() - 1).getMarkerObj());
                this.map.addLayer(lLayerGroupRoutes);
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


    public void updateZone(Zone zone) {
        zone.getPolygon().removeFrom(this.map);
        switch (zone.getEmergencyLevel()) {
            case "LOW":
                zone.generatePolygon(reg, "grey", "green");
                break;
            case "MEDIUM":
                zone.generatePolygon(reg, "grey", "yellow");
                break;
            case "HIGH":
                zone.generatePolygon(reg, "grey", "red");
                break;
            case "VERYHIGH":
                zone.generatePolygon(reg, "grey", "black");
                break;
            default:
                zone.generatePolygon(reg, "grey", "blue");
        }
        zone.getPolygon().bindPopup(zone.getName());
        zone.getPolygon().addTo(this.map);
    }

    public void updateRoute(Route route) {
        route.getPolygon().removeFrom(this.map);
        switch (route.getRouteType()) {
            case "PREFERRED_ROUTE":
                route.generatePolygon(reg, "green", "green");
                break;
            case "LOW_RISK":
                route.generatePolygon(reg, "yellow", "yellow");
                break;
            case "MEDIUM_RISK":
                route.generatePolygon(reg, "red", "red");
                break;
            case "HIGH_RISK":
                route.generatePolygon(reg, "black", "black");
                break;
            case "CLOSED":
                route.generatePolygon(reg, "blue", "blue");
                break;
            default:
                route.generatePolygon(reg, "blue", "blue");
        }
        route.getPolygon().bindPopup(route.getName());
        route.getPolygon().addTo(this.map);
        for (RoutePoint routePoint : this.routePoints.get(route.getID())) {
            routePoint.getMarkerObj().bindPopup(route.getName());
        }
    }

}
