package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues;


import com.vaadin.flow.component.UI;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.*;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateRouteCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateStorageCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateZoneCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ShowEvent;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.mapObjects.factories.*;
import org.pinguweb.frontend.services.BackendDTOService;
import org.pinguweb.frontend.utils.Mediador.ComponentColleague;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;
import org.springframework.scheduling.annotation.Async;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.controls.LControlLayers;
import software.xdev.vaadin.maps.leaflet.controls.LControlLayersOptions;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.LLayerGroup;

import java.util.*;

@Slf4j
@Setter
@Getter
public class Service extends ComponentColleague {

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

    public Service(Map mediator) {
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
        mediator.subscribe(EventType.DELETE, this);
        mediator.subscribe(EventType.EDIT, this);
    }

    @Override
    public <T> void receive(Event<T> event) {
        switch (event.getType()){
            case LOAD -> load();
            case CREATE -> {
                if (event.getPayload() instanceof StorageDTO){
                    Storage s = createStorage((StorageDTO) event.getPayload());
                    s.setID(s.pushToServer());
                    ((CreateStorageCommand) event.getCommand()).setStorage(s);
                    event.getCommand().endExecution();
                    mediator.publish(new ShowEvent<>(EventType.SHOW, s, null));
                }
                else if (event.getPayload() instanceof RouteDTO){
                    List<RoutePoint> points = ((CreationEvent<T>)event).getExtraData();
                    Route r = createRoute((RouteDTO) event.getPayload(), points);
                    r.setID(r.pushToServer());
                    ((CreateRouteCommand) event.getCommand()).setRoute(r);
                    for (RoutePoint p :  points){
                        p.pushToServer();
                    }
                    ((CreateRouteCommand) event.getCommand()).setPoints(points);
                    mediator.publish(new CreationEvent<>(EventType.SHOW, r, null, points));
                }
                else if (event.getPayload() instanceof ZoneDTO){
                    Zone z = createZone((ZoneDTO) event.getPayload());
                    z.setID(z.pushToServer());
                    ((CreateZoneCommand) event.getCommand()).setZone(z);
                    event.getCommand().endExecution();
                    mediator.publish(new ShowEvent<>(EventType.SHOW, z, null));
                }
            }
            case DELETE -> {
                if (event.getPayload() instanceof StorageDTO){
                    deleteStorage(((StorageDTO) event.getPayload()).getID());
                }
                else if (event.getPayload() instanceof RouteDTO){
                    deleteRoute(((RouteDTO) event.getPayload()).getID());
                }
                else if (event.getPayload() instanceof ZoneDTO){
                    deleteZone(((ZoneDTO) event.getPayload()).getID());
                }
            }
            case EDIT -> {
                if (event.getPayload() instanceof Storage){
                    updateStorage((Storage) event.getPayload());
                }
                else if (event.getPayload() instanceof Route){
                    updateRoute((Route) event.getPayload());
                }
                else if (event.getPayload() instanceof Zone){
                    updateZone((Zone) event.getPayload());
                }
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

        loadNeeds(ui);
        loadZones(ui);
        loadRoutes(ui);
        loadStorages(ui);
    }

    private void loadNeeds(UI ui) {
        for (NeedDTO need : backendService.getNeedList().getValues()) {
            if (need.getLatitude() != null && need.getLongitude() != null) {
                ui.access(() -> {
                    log.debug(need.toString());
                    mediator.publish(new CreationEvent<>(EventType.SHOW, createNeed(need), null, null));
                });
            }
        }
    }

    private void loadZones(UI ui) {
        for (ZoneDTO zone : backendService.getZoneList().getValues()) {
            if (!zone.getLatitudes().isEmpty() && !zone.getLongitudes().isEmpty()) {
                ui.access(() -> mediator.publish(new CreationEvent<>(EventType.SHOW, createZone(zone), null, null)));
            }
        }
    }

    private void loadStorages(UI ui) {
        for (StorageDTO storage : backendService.getStorageList().getValues()) {
            if (storage.getLatitude() != null && storage.getLongitude() != null) {
                ui.access(() -> mediator.publish(new CreationEvent<>(EventType.SHOW, createStorage(storage), null, null)));
            }
        }
    }

    private void loadRoutes(UI ui) {
        for (RouteDTO route : backendService.getRouteList().getValues()) {
            if (!route.getPoints().isEmpty() && !backendService.getRoutePointList().isEmpty()) {
                List<RoutePoint> routePoints = new ArrayList<>();

                for (Integer i : route.getPoints()) {
                    Optional<RoutePointDTO> routePoint = backendService.getRoutePointList().getValues().stream().filter(
                            x -> x.getID() == i
                    ).findFirst();

                    if (routePoint.isPresent()){
                        double lat = routePoint.get().getLatitude();
                        double lon = routePoint.get().getLongitude();
                        int id = routePoint.get().getID();

                        RoutePoint point = (RoutePoint) routePointFactory.createMapObject(this.map.getReg(), lat, lon);
                        point.setID(id);
                        point.getMarkerObj().bindPopup("Ruta: " + route.getName());
                        this.map.getRoutePoints().computeIfAbsent(route.getID(), k -> new ArrayList<>()).add(point);

                        routePoints.add(point);
                    }
                }

                if (!routePoints.isEmpty()) {
                   ui.access(() -> mediator.publish(new CreationEvent<>(EventType.SHOW, createRoute(route, routePoints), null, routePoints)));
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

    private void addControls() {
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

        return storageObj;
    }

    private Zone createZone(ZoneDTO zoneDTO) {
        List<Tuple<Double, Double>> points = new ArrayList<>();

        for (int i = 0; i < zoneDTO.getLatitudes().size(); i++) {
            points.add(new Tuple<>(zoneDTO.getLatitudes().get(i), zoneDTO.getLongitudes().get(i)));
        }

        Zone zone = (Zone) zoneFactory.createMapObject(this.map.getReg(), 0.0, zoneDTO.getID() + 1.0);
        for (Tuple<Double, Double> marker : points) {
            zone.addPoint(this.map.getReg(), marker);
        }

        zone.setName(zoneDTO.getName() != null ? zoneDTO.getName() : "Sin nombre");
        zone.setID(zoneDTO.getID());
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

        return zone;
    }

    private Route createRoute(RouteDTO routeDTO, List<RoutePoint> routePoints) {
        Route route = (Route) routeFactory.createMapObject(this.map.getReg(), 0.0, routeDTO.getID() + 1.0);
        for (RoutePoint routePoint : routePoints) {
            route.addPoint(this.map.getReg(), new Tuple<>(routePoint.getLatitude(), routePoint.getLongitude()));
            routePoint.removeFromMap(map.getMap());
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
        route.setPointsID(routeDTO.getPoints());

        route.getPolygon().bindPopup("Ruta: " + route.getName());

        map.getNewRoutePoints().clear();

        return route;
    }

    private Need createNeed(NeedDTO needDTO) {
        double lat = needDTO.getLatitude();
        double lng = needDTO.getLongitude();
        Need need = (Need) needFactory.createMapObject(this.map.getReg(), lat, lng);
        need.setID(needDTO.getID());
        need.addToMap(this.map.getMap());

        need.getMarkerObj().bindPopup(needDTO.getNeedType());
        map.getNeeds().add(need);
        map.getLLayerGroupNeeds().addLayer(need.getMarkerObj());
        this.map.getMap().addLayer(map.getLLayerGroupNeeds());

        return need;
    }

    private void deleteZone(int ID) {
        Zone zone = map.getZones().stream()
                .filter(z -> z.getID() == ID)
                .findFirst()
                .orElse(null);
        if (zone != null) {
            zone.removeFromMap(this.map.getMap());
            map.getZones().remove(zone);
            map.getLLayerGroupZones().removeLayer(zone.getPolygon());
            this.map.getMap().addLayer(map.getLLayerGroupZones());

            zone.deleteFromServer();
            backendService.getZoneList().update();
        }
    }

    private void deleteRoute(int ID) {
        Route route = map.getRoutes().stream()
                .filter(r -> r.getID() == ID)
                .findFirst()
                .orElse(null);

        if (route != null) {
            route.removeFromMap(this.map.getMap());
            route.deleteFromServer();
            List<RoutePoint> routePoints = map.getRoutePoints().get(route.getID());
            if (routePoints != null) {
                for (RoutePoint routePoint : routePoints) {
                    routePoint.deleteFromServer();
                    routePoint.removeFromMap(this.map.getMap());
                }

                map.getRoutes().remove(route);
                map.getLLayerGroupRoutes().removeLayer(route.getPolygon());
                map.getLLayerGroupRoutes().removeLayer(routePoints.get(0).getMarkerObj());
                map.getLLayerGroupRoutes().removeLayer(routePoints.get(routePoints.size() - 1).getMarkerObj());
                this.map.getMap().addLayer(map.getLLayerGroupRoutes());
                map.getRoutePoints().remove(route.getID());

                backendService.getRoutePointList().update();
                backendService.getRouteList().update();
            }
        }
    }

    private void deleteStorage(int i) {
        Storage storage = map.getStorages().stream()
                .filter(s -> s.getID() == i)
                .findFirst()
                .orElse(null);
        if (storage != null) {
            map.getStorages().remove(storage);
            map.getLLayerGroupStorages().removeLayer(storage.getMarkerObj());
            this.map.getMap().addLayer(map.getLLayerGroupStorages());
            storage.removeFromMap(this.map.getMap());

            storage.deleteFromServer();
            backendService.getStorageList().update();
        }
    }

    private void updateZone(Zone zone) {
        zone.updateToServer();

        map.getZones().stream().filter(z -> Objects.equals(z.getID(), zone.getID())).findFirst().ifPresent(z -> {
            map.getZones().remove(z);
            map.getZones().add(zone);
        });

        zone.getPolygon().removeFrom(this.map.getMap());
        switch (zone.getEmergencyLevel()) {
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
        zone.getPolygon().addTo(this.map.getMap());
        backendService.getZoneList().update();
    }

    private void updateRoute(Route route) {
        route.updateToServer();

        map.getRoutes().stream().filter(r -> Objects.equals(r.getID(), route.getID())).findFirst().ifPresent(r -> {
            map.getRoutes().remove(r);
            map.getRoutes().add(route);
        });

        route.getPolygon().removeFrom(this.map.getMap());
        switch (route.getRouteType()) {
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
                route.generatePolygon(this.map.getReg(), "blue", "blue");
        }
        route.getPolygon().bindPopup("Ruta: " + route.getName());
        route.getPolygon().addTo(this.map.getMap());
        for (RoutePoint routePoint : map.getRoutePoints().get(route.getID())) {
            routePoint.getMarkerObj().bindPopup("Ruta: " + route.getName());
        }
        backendService.getRouteList().update();
    }

    private void updateStorage(Storage storage) {
        storage.updateToServer();
        map.getStorages().stream().filter(s -> Objects.equals(s.getID(), storage.getID())).findFirst().ifPresent(s -> {
            map.getStorages().remove(s);
            map.getStorages().add(storage);
        });
        storage.getMarkerObj().removeFrom(this.map.getMap());
        storage.getMarkerObj().bindPopup("Almacen: " + storage.getName());
        storage.getMarkerObj().addTo(this.map.getMap());
        backendService.getStorageList().update();
    }

}
