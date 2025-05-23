package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues;


import com.vaadin.flow.component.UI;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.*;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.DeleteCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.EditCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateStorageCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.mapObjects.factories.*;
import org.pinguweb.frontend.services.BackendDTOService;
import org.pinguweb.frontend.utils.Mediador.Colleague;
import org.pinguweb.frontend.utils.Mediador.ComponentColleague;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.controls.LControlLayers;
import software.xdev.vaadin.maps.leaflet.controls.LControlLayersOptions;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.LLayerGroup;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Setter
@Getter
public class MapService extends ComponentColleague {

    private Object lock = new Object();
    private UI ui;

    private Tuple<Double, Double> routePointStartingPoint;
    private List<RoutePoint> routePoint = new ArrayList<>();

    private Map map;
    private final BackendDTOService backendService = BackendDTOService.GetInstancia();

    private NeedFactory needFactory;
    private ZoneFactory zoneFactory;
    private ZoneMarkerFactory zoneMarkerFactory;
    private RouteFactory routeFactory;
    private RoutePointFactory routePointFactory;
    private StorageFactory storageFactory;

    public MapService(Map mediator) {
        super(mediator);
        this.map = mediator;

        this.needFactory = new NeedFactory();
        this.zoneFactory = new ZoneFactory();
        this.routeFactory = new RouteFactory();
        this.routePointFactory = new RoutePointFactory();
        this.storageFactory = new StorageFactory();
    }

    @Override
    public void register() {
        mediator.subscribe(EventType.LOAD, this);
        mediator.subscribe(EventType.CREATE, this);
    }

    @Override
    public <T> void receive(Event<T> event) {
        if(event.getType() == EventType.LOAD){
            load();
        }
        if (event.getType() == EventType.CREATE){
            if (event.getPayload() instanceof StorageDTO){
                createStorage((StorageDTO) event.getPayload());
            }
            else if (event.getPayload() instanceof RouteDTO){

            }
            else if (event.getPayload() instanceof ZoneDTO){

            }
        }
    }

    @Async
    public void load() {
        if (this.map.getLLayerGroupStorages() != null)
        {
            this.map.getLLayerGroupStorages().clearLayers();
            this.map.getLLayerGroupRoutes().clearLayers();
            this.map.getLLayerGroupNeeds().clearLayers();
            this.map.getLLayerGroupZones().clearLayers();
        }
        else{
            generateLayers();
        }

        UI ui = UI.getCurrent();
        if (ui == null) {
            log.warn("UI is null, cannot update UI components.");
            return;
        }

        CompletableFuture.runAsync(() -> loadNeeds(ui));
        CompletableFuture.runAsync(() -> loadZones(ui));
        CompletableFuture.runAsync(() -> loadRoutes(ui));
        CompletableFuture.runAsync(() -> loadStorages(ui));

    }

    private void loadNeeds(UI ui) {
        for (NeedDTO need : backendService.getNeedList().getValues()) {
            if (need.getLatitude() != null && need.getLongitude() != null) {
                ui.access(() -> {
                    log.debug(need.toString());
                    mediator.publish(new CreationEvent<>(EventType.SHOW, need, null, null));
                });
            }
        }
    }

    private void loadZones(UI ui) {
        for (ZoneDTO zone : backendService.getZoneList().getValues()) {
            if (!zone.getLatitudes().isEmpty() && !zone.getLongitudes().isEmpty()) {
                ui.access(() -> mediator.publish(new CreationEvent<>(EventType.SHOW, zone, null, null)));
            }
        }
    }

    private void loadStorages(UI ui) {
        for (StorageDTO storage : backendService.getStorageList().getValues()) {
            if (storage.getLatitude() != null && storage.getLongitude() != null) {
                ui.access(() -> mediator.publish(new CreationEvent<>(EventType.SHOW, storage, null, null)));
            }
        }
    }

    private void loadRoutes(UI ui) {
        for (RouteDTO route : backendService.getRouteList().getValues()) {
            if (!route.getPoints().isEmpty()) {
                List<RoutePoint> routePoints = new ArrayList<>();
                for (Integer i : route.getPoints()) {
                    RoutePointDTO routePoint = backendService.getRoutePointList().getValues().stream().filter(
                            routePointDTO ->  route.getPoints().get(i) == routePointDTO.getID()
                    ).toList().get(0);
                    if (routePoint != null) {
                        double lat = routePoint.getLatitude();
                        double lon = routePoint.getLongitude();
                        int id = routePoint.getID();
                        boolean isEdge = i == 0 || i == route.getPoints().size() - 1;

                        ui.access(() -> {
                            RoutePoint point = (RoutePoint) routePointFactory.createMapObject(this.map.getReg(), lat, lon);
                            point.setID(id);
                            point.getMarkerObj().bindPopup("Ruta: " + route.getName());
                            this.map.getRoutePoints().computeIfAbsent(route.getID(), k -> new ArrayList<>()).add(point);
                            if (isEdge) {
                                point.addToMap(this.map.getMap());
                            }
                            routePoints.add(point);
                        });

                    } else {
                        log.debug("RoutePoint not found: " + route.getPoints().get(i));
                    }
                }

                if (!routePoints.isEmpty()) {
                    ui.access(() -> mediator.publish(
                            new CreationEvent<>(EventType.SHOW, route, null, routePoints))
                    );
                }
            }

        }
    }

    private void generateLayers() {
        this.map.setLLayerGroupNeeds(new LLayerGroup(this.map.getReg()));
        this.map.setLLayerGroupZones(new LLayerGroup(this.map.getReg()));
        this.map.setLLayerGroupRoutes(new LLayerGroup(this.map.getReg()));
        this.map.setLLayerGroupStorages(new LLayerGroup(this.map.getReg()));
        addControls();
    }

    public void addControls() {
        final LinkedHashMap<String, LLayer<?>> baseLayers = new LinkedHashMap<>();
        final LControlLayers lControlLayers = new LControlLayers(
                this.map.getReg(),
                baseLayers,
                new LControlLayersOptions().withCollapsed(true))
                .addOverlay(this.map.getLLayerGroupNeeds(), "Necesidades")
                .addOverlay(this.map.getLLayerGroupZones(), "Zonas")
                .addOverlay(this.map.getLLayerGroupRoutes(), "Rutas")
                .addOverlay(this.map.getLLayerGroupStorages(), "Almacenes")
                .addTo(map.getMap());

        this.map.getMap().addControl(lControlLayers);
        this.map.getMap().addLayer(this.map.getLLayerGroupNeeds())
                .addLayer(this.map.getLLayerGroupZones())
                .addLayer(this.map.getLLayerGroupRoutes())
                .addLayer(this.map.getLLayerGroupStorages());
    }



    private Storage createStorage(StorageDTO storage) {
        double lat = storage.getLatitude();
        double lng = storage.getLongitude();
        Storage storageObj = (Storage) storageFactory.createMapObject(this.map.getReg(), lat, lng);
        storageObj.setID(storage.getID());
        storageObj.setName(storage.getName() != null ? storage.getName() : "Sin nombre");
        storageObj.setFull(storage.isFull());
        storageObj.setLatitude(storage.getLatitude());
        storageObj.setLongitude(storage.getLongitude());
        storageObj.setZoneID(storage.getZone() != null ? storage.getZone() : -1);
        storageObj.addToMap(this.map.getMap());
        storageObj.getMarkerObj().bindPopup("Almacen: " + storage.getName());

        map.getStorages().add(storageObj);
        map.getLLayerGroupStorages().addLayer(storageObj.getMarkerObj());
        this.map.getMap().addLayer(map.getLLayerGroupStorages());
        return storageObj;
    }

    public Zone createZone(ZoneDTO zoneDTO) {
        List<Tuple<Double, Double>> points = new ArrayList<>();

        for (int i = 0; i < zoneDTO.getLatitudes().size(); i++) {
            points.add(new Tuple<>(zoneDTO.getLatitudes().get(i), zoneDTO.getLongitudes().get(i)));
        }

        Zone zone = (Zone) zoneFactory.createMapObject(this.map.getReg(), 0.0, zoneDTO.getID() + 1.0);
        for (Tuple<Double, Double> marker : points) {
            zone.addPoint(this.map.getReg(), marker);
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
                zone.generatePolygon(this.map.getReg(), "grey", "green");
                break;
            case "MEDIUM":
                zone.generatePolygon(this.map.getReg(), "grey", "yellow");
                break;
            case "HIGH":
                zone.generatePolygon(this.map.getReg(), "grey", "red");
                break;
            case "VERYHIGH":
                zone.generatePolygon(this.map.getReg(), "grey", "black");
                break;
            default:
                zone.generatePolygon(this.map.getReg(), "grey", "blue");
        }
        zone.getPolygon().bindPopup("Zona: " + zone.getName());
        zone.setID(zoneDTO.getID());
        zone.addToMap(this.map.getMap());

        map.getZones().add(zone);
        map.getLLayerGroupZones().addLayer(zone.getPolygon());
        this.map.getMap().addLayer(map.getLLayerGroupZones());

        return zone;
    }

    public Route createRoute(RouteDTO routeDTO, List<RoutePoint> routePoints) {
        Route route = (Route) routeFactory.createMapObject(this.map.getReg(), 0.0, routeDTO.getID() + 1.0);
        route.setID(routeDTO.getID());
        for (RoutePoint routePoint : routePoints) {
            route.addPoint(this.map.getReg(), new Tuple<>(routePoint.getLatitude(), routePoint.getLongitude()));
        }

        switch (routeDTO.getRouteType()) {
            case "PREFERRED_ROUTE":
                route.generatePolygon(this.map.getReg(), "green", "green");
                break;
            case "LOW_RISK":
                route.generatePolygon(this.map.getReg(), "yellow", "yellow");
                break;
            case "MEDIUM_RISK":
                route.generatePolygon(this.map.getReg(), "red", "red");
                break;
            case "HIGH_RISK":
                route.generatePolygon(this.map.getReg(), "black", "black");
                break;
            case "CLOSED":
                route.generatePolygon(this.map.getReg(), "blue", "blue");
                break;
            default:
                route.generatePolygon(this.map.getReg(), "black", "black");
        }

        route.setID(routeDTO.getID());
        route.setRouteType(routeDTO.getRouteType());
        route.setName(routeDTO.getName() != null ? routeDTO.getName() : "Sin nombre");
        route.setCatastrophe(routeDTO.getCatastrophe() != null ? routeDTO.getCatastrophe() : -1); // -1 podría indicar "sin catástrofe"
        route.setPointsID(routeDTO.getPoints() != null ? (ArrayList<Integer>) routeDTO.getPoints() : new ArrayList<>());

        route.getPolygon().bindPopup("Ruta: " + route.getName());
        map.getRoutes().add(route);

        map.getLLayerGroupRoutes().addLayer(route.getPolygon());
        map.getLLayerGroupRoutes().addLayer(routePoints.get(0).getMarkerObj());
        map.getLLayerGroupRoutes().addLayer(routePoints.get(routePoints.size() - 1).getMarkerObj());
        routePoints.get(0).getMarkerObj().bindPopup("Ruta: " + route.getName());
        routePoints.get(routePoints.size() - 1).getMarkerObj().bindPopup("Ruta: " + route.getName());
        this.map.getMap().addLayer(map.getLLayerGroupRoutes());

        return route;
    }


//
//    public Need createNeed(NeedDTO needDTO) {
//        double lat = needDTO.getLatitude();
//        double lng = needDTO.getLongitude();
//        Need need = (Need) needFactory.createMapObject(this.map.getReg(), lat, lng);
//        need.setID(needDTO.getID());
//        need.addToMap(this.map.getMap());
//
//        need.getMarkerObj().bindPopup(needDTO.getNeedType());
//        needs.add(need);
//        lLayerGroupNeeds.addLayer(need.getMarkerObj());
//        this.map.getMap().addLayer(lLayerGroupNeeds);
//
//        for (Need m : needs) {
//            log.debug("ID: " + m.getID() + m);
//        }
//        log.debug("Fin");
//
//        return need;
//    }
//
//    public void deleteNeed(int ID) {
//        Need need = needs.stream()
//                .filter(m -> m.getID() == ID)
//                .findFirst()
//                .orElse(null);
//        if (need != null) {
//            need.removeFromMap(this.map.getMap());
//            need.deleteFromServer();
//            needs.remove(need);
//            lLayerGroupNeeds.removeLayer(need.getMarkerObj());
//            this.map.getMap().addLayer(lLayerGroupNeeds);
//        }
//    }
//
//    public void deleteZone(int ID) {
//        Zone zone = zones.stream()
//                .filter(z -> z.getID() == ID)
//                .findFirst()
//                .orElse(null);
//        if (zone != null) {
//            if (tempDeleteCommand != null){tempDeleteCommand.setElement(zone);}
//            zone.removeFromMap(this.map.getMap());
//            zone.deleteFromServer();
//            zones.remove(zone);
//            lLayerGroupZones.removeLayer(zone.getPolygon());
//            this.map.getMap().addLayer(lLayerGroupZones);
//        }
//    }
//
//    public void deleteRoute(int ID) {
//        Route route = routes.stream()
//                .filter(r -> r.getID() == ID)
//                .findFirst()
//                .orElse(null);
//
//        if (route != null) {
//            if (tempDeleteCommand != null) {tempDeleteCommand.setElement(route);}
//            route.removeFromMap(this.map.getMap());
//            route.deleteFromServer();
//            List<RoutePoint> routePoints = this.routePoints.get(route.getID());
//            if (routePoints != null) {
//                if (tempDeleteCommand != null) {tempDeleteCommand.setPoints(routePoints);}
//                for (RoutePoint routePoint : routePoints) {
//                    routePoint.deleteFromServer();
//                    routePoint.removeFromMap(this.map.getMap());
//                }
//                routes.remove(route);
//                lLayerGroupRoutes.removeLayer(route.getPolygon());
//                lLayerGroupRoutes.removeLayer(routePoints.get(0).getMarkerObj());
//                lLayerGroupRoutes.removeLayer(routePoints.get(routePoints.size() - 1).getMarkerObj());
//                this.map.getMap().addLayer(lLayerGroupRoutes);
//                this.routePoints.remove(route.getID());
//            }
//            log.debug("Route deleted: {}", route.getID());
//        }
//    }
//
//    public Need getNeedByID(String ID) {
//        return needs.stream()
//                .filter(m -> m.getID() == Integer.parseInt(ID))
//                .findFirst()
//                .orElse(null);
//    }
//
//    public Zone getZoneByID(String ID) {
//        return zones.stream()
//                .filter(z -> z.getID() == Integer.parseInt(ID))
//                .findFirst()
//                .orElse(null);
//    }
//
//    public Route getRouteByID(String ID) {
//        return routes.stream()
//                .filter(r -> r.getID() == Integer.parseInt(ID))
//                .findFirst()
//                .orElse(null);
//    }
//
//
//    public void updateZone(Zone zone) {
//        zone.getPolygon().removeFrom(this.map.getMap());
//        switch (zone.getEmergencyLevel()) {
//            case "LOW":
//                zone.generatePolygon(this.map.getReg(), "grey", "green");
//                break;
//            case "MEDIUM":
//                zone.generatePolygon(this.map.getReg(), "grey", "yellow");
//                break;
//            case "HIGH":
//                zone.generatePolygon(this.map.getReg(), "grey", "red");
//                break;
//            case "VERYHIGH":
//                zone.generatePolygon(this.map.getReg(), "grey", "black");
//                break;
//            default:
//                zone.generatePolygon(this.map.getReg(), "grey", "blue");
//        }
//        zone.getPolygon().bindPopup("Zona: " + zone.getName());
//        zone.getPolygon().addTo(this.map.getMap());
//    }
//
//    public void updateRoute(Route route) {
//        route.getPolygon().removeFrom(this.map.getMap());
//        switch (route.getRouteType()) {
//            case "PREFERRED_ROUTE":
//                route.generatePolygon(this.map.getReg(), "green", "green");
//                break;
//            case "LOW_RISK":
//                route.generatePolygon(this.map.getReg(), "yellow", "yellow");
//                break;
//            case "MEDIUM_RISK":
//                route.generatePolygon(this.map.getReg(), "red", "red");
//                break;
//            case "HIGH_RISK":
//                route.generatePolygon(this.map.getReg(), "black", "black");
//                break;
//            case "CLOSED":
//                route.generatePolygon(this.map.getReg(), "blue", "blue");
//                break;
//            default:
//                route.generatePolygon(this.map.getReg(), "blue", "blue");
//        }
//        route.getPolygon().bindPopup("Ruta: " + route.getName());
//        route.getPolygon().addTo(this.map.getMap());
//        for (RoutePoint routePoint : this.routePoints.get(route.getID())) {
//            routePoint.getMarkerObj().bindPopup("Ruta: " + route.getName());
//        }
//    }
//
//    public void deleteStorage(int i) {
//        Storage storage = storages.stream()
//                .filter(s -> s.getID() == i)
//                .findFirst()
//                .orElse(null);
//        if (storage != null) {
//            if (tempDeleteCommand != null) {tempDeleteCommand.setElement(storage);}
//            storage.removeFromMap(this.map.getMap());
//            storage.deleteFromServer();
//            storages.remove(storage);
//            lLayerGroupStorages.removeLayer(storage.getMarkerObj());
//            this.map.getMap().addLayer(lLayerGroupStorages);
//        }
//    }
//
//    public Storage getStorageByID(String id) {
//        return storages.stream()
//                .filter(s -> s.getID() == Integer.parseInt(id))
//                .findFirst()
//                .orElse(null);
//    }
//
//    public void updateStorage(Storage storage) {
//        storage.getMarkerObj().removeFrom(this.map.getMap());
//        storage.getMarkerObj().bindPopup("Almacen: " + storage.getName());
//        storage.getMarkerObj().addTo(this.map.getMap());
//    }

}
