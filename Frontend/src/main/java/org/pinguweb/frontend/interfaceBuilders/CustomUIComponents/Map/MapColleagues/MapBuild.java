package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues;

import com.vaadin.flow.component.notification.Notification;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.RouteDTO;
import org.pingu.domain.DTO.StorageDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.*;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.mapObjects.factories.*;
import org.pinguweb.frontend.utils.Mediador.Colleague;
import org.pinguweb.frontend.utils.Mediador.ComponentColleague;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;
import org.pinguweb.frontend.view.MapView;
import org.yaml.snakeyaml.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Setter
@Getter
public class MapBuild extends ComponentColleague {
    private String clickFuncReferenceCreateZone;
    private String clickFuncReferenceCreateRoute;
    private String clickFuncReferenceCreateStorage;
    private Map map;

    private NeedFactory needFactory;
    private ZoneFactory zoneFactory;
    private ZoneMarkerFactory zoneMarkerFactory;
    private RouteFactory routeFactory;
    private RoutePointFactory routePointFactory;
    private StorageFactory storageFactory;

    public MapBuild(Map map) {
        super(map);
        this.map = map;
        clickFuncReferenceCreateZone = map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateZone";
        clickFuncReferenceCreateRoute = map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateRoute";
        clickFuncReferenceCreateRoute = map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateStorage";

        this.needFactory = new NeedFactory();
        this.zoneFactory = new ZoneFactory();
        this.zoneMarkerFactory = new ZoneMarkerFactory();
        this.routeFactory = new RouteFactory();
        this.routePointFactory = new RoutePointFactory();
        this.storageFactory = new StorageFactory();
    }

    @Override
    public void register() {
        mediator.subscribe(EventType.BUILD, this);
    }

    @Override
    public <T> void receive(Event<T> event) {
        if (event.getPayload() instanceof StorageDTO){
            createStorage((StorageDTO) event.getPayload(), (CreateStorageCommand) event.getCommand());
        }
        else if (event.getPayload() instanceof ZoneDTO){
            createZone((ZoneDTO) event.getPayload(), (CreateZoneCommand) event.getCommand());
        }
        else if (event.getPayload() instanceof RouteDTO){
            createRoute((RouteDTO) event.getPayload(), ((CreationEvent<T>) event).getExtraData(), (CreateRouteCommand) event.getCommand());
        }
    }

    private Storage createStorageObject(StorageDTO storage) {
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
//        this.map.getMap().addLayer(lLayerGroupStorages);
        return storageObj;
    }

    public void createStorage(StorageDTO dto, CreateStorageCommand c){
        Storage storage = createStorageObject(dto);

        if (c != null ) {c.setStorage(storage);}
        int tempId = storage.pushToServer();
        storage.setID(tempId);
        map.getStorages().stream().filter(s -> Objects.equals(s.getID(), storage.getID())).findFirst().ifPresent(s -> {
            map.getStorages().remove(s);
        });

        map.getMap().off("click", clickFuncReferenceCreateStorage);

        Notification notification = new Notification("Almacén creado exitosamente", 3000);
        notification.open();
    }


    public void createZone(ZoneDTO zone, CreateZoneCommand c) {
        log.debug("Zona terminada");
        Zone zona = createZone(zone);

        int newID = zona.pushToServer();
        map.getZones().stream().filter(z -> z.getID() == zone.getID()).findFirst().ifPresent(z -> {
            z.setID(newID);
        });

        for (ZoneMarker zoneMarker : map.getZoneMarkers().values()) {
            zoneMarker.removeFromMap(map.getMap());
        }

        map.getMap().off("click", clickFuncReferenceCreateZone);
        map.getZoneMarkers().clear();
        map.getZoneMarkerPoints().clear();
        if (c != null) {c.setZone(zona);}
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
//        this.map.getMap().addLayer(lLayerGroupZones);

        return zone;
    }

    public ZoneMarker createZoneMarker(double lat, double lng) {
        ZoneMarker zoneMarker = (ZoneMarker) zoneMarkerFactory.createMapObject(this.map.getReg(), lat, lng);

        String clickFuncReferenceDragStart = this.map.getMap().clientComponentJsAccessor() + ".myClickFuncDragStart" + zoneMarker.getID();
        String clickFuncReferenceDragEnd = this.map.getMap().clientComponentJsAccessor() + ".myClickFuncDragEnd" + zoneMarker.getID();

        this.map.getReg().execJs(clickFuncReferenceDragStart + "=e => document.getElementById('" + MapView.getMapId() + "').$server.routePointStart(e.target.getLatLng())");
        this.map.getReg().execJs(clickFuncReferenceDragEnd + "=e => document.getElementById('" + MapView.getMapId() + "').$server.routePointEnd(e.target.getLatLng())");

        zoneMarker.getMarkerObj().on("dragstart", clickFuncReferenceDragStart);
        zoneMarker.getMarkerObj().on("dragend", clickFuncReferenceDragEnd);

        zoneMarker.addToMap(this.map.getMap());

        return zoneMarker;
    }

//    public void editZone(Zone zone) {
//        log.debug("Zona editada");
//        zone.updateToServer();
//        service.getZones().stream().filter(z -> Objects.equals(z.getID(), zone.getID())).findFirst().ifPresent(z -> {
//            service.getZones().remove(z);
//            service.getZones().add(zone);
//        });
//        service.updateZone(zone);
//    }


    public void createRoute(RouteDTO route, List<RoutePoint> points, CreateRouteCommand c) {
        map.getMap().off("click", clickFuncReferenceCreateRoute);
        log.debug("Ruta terminada");
        List<Integer> pointsID = new ArrayList<>();
        for (RoutePoint routePoint : points) {
            pointsID.add(routePoint.pushToServer());
        }

        if (c != null) {c.setPoints(points);}

        Route ruta = createRoute(route, points);
        ruta.setPointsID(pointsID);
        int tempID = ruta.pushToServer();

        if (c != null) {c.setRoute(ruta);}

        map.getRoutes().stream().filter(r -> r.getID() == route.getID()).findFirst().ifPresent(r -> {
            map.getRoutes().remove(r);
            map.getRoutes().add(ruta);
        });
        map.getRoutePoints().put(tempID, new ArrayList<>(points));

        for (int i = points.size() - 2; i > 0; i--) {
            RoutePoint routePoint = points.get(i);
            routePoint.removeFromMap(map.getMap());
        }
    }

    public RoutePoint createRoutePoint(double lat, double lng) {
        RoutePoint routePoint = (RoutePoint) routePointFactory.createMapObject(this.map.getReg(), lat, lng);

        String clickFuncReferenceDragStart = this.map.getMap().clientComponentJsAccessor() + ".myClickFuncDragStart" + routePoint.getID();
        String clickFuncReferenceDragEnd = this.map.getMap().clientComponentJsAccessor() + ".myClickFuncDragEnd" + routePoint.getID();

        this.map.getReg().execJs(clickFuncReferenceDragStart + "=e => document.getElementById('" + MapView.getMapId() + "').$server.routePointStart(e.target.getLatLng())");
        this.map.getReg().execJs(clickFuncReferenceDragEnd + "=e => document.getElementById('" + MapView.getMapId() + "').$server.routePointEnd(e.target.getLatLng())");

        routePoint.getMarkerObj().on("dragstart", clickFuncReferenceDragStart);
        routePoint.getMarkerObj().on("dragend", clickFuncReferenceDragEnd);

        routePoint.addToMap(this.map.getMap());

        return routePoint;
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
//        this.map.getMap().addLayer(lLayerGroupRoutes);

        return route;
    }

//
//    public void editRoute(org.pinguweb.frontend.mapObjects.Route route) {
//        log.debug("Ruta editada");
//        route.updateToServer();
//        service.getRoutes().stream().filter(r -> Objects.equals(r.getID(), route.getID())).findFirst().ifPresent(r -> {
//            service.getRoutes().remove(r);
//            service.getRoutes().add(route);
//        });
//        service.updateRoute(route);
//    }
//
//    public void startEdit() {
//        for (Need need : this.service.getNeeds()) {
//            String clickFuncReferenceEditMarker = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditMarker" + need.getID();
//            this.service.getReg().execJs(clickFuncReferenceEditMarker + "=e => document.getElementById('" + this.service.getID() + "').$server.editMarker('" + need.getID() + "')");
//            need.getMarkerObj().on("click", clickFuncReferenceEditMarker);
//        }
//        for (Zone zone : this.service.getZones()) {
//            String clickFuncReferenceEditZone = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditZone" + zone.getID();
//            this.service.getReg().execJs(clickFuncReferenceEditZone + "=e => document.getElementById('" + this.service.getID() + "').$server.editPolygon('" + zone.getID() + "') ");
//            zone.getPolygon().on("click", clickFuncReferenceEditZone);
//        }
//        for (org.pinguweb.frontend.mapObjects.Route route : this.service.getRoutes()) {
//            String clickFuncReferenceEditRoute = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditRoute" + route.getID();
//            this.service.getReg().execJs(clickFuncReferenceEditRoute + "=e => document.getElementById('" + this.service.getID() + "').$server.editRoute('" + route.getID() + "') ");
//            route.getPolygon().on("click", clickFuncReferenceEditRoute);
//        }
//        for (Storage storage : this.service.getStorages()) {
//            String clickFuncReferenceEditStorage = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditStorage" + storage.getID();
//            this.service.getReg().execJs(clickFuncReferenceEditStorage + "=e => document.getElementById('" + this.service.getID() + "').$server.editStorage('" + storage.getID() + "') ");
//            storage.getMarkerObj().on("click", clickFuncReferenceEditStorage);
//        }
//    }
//
//    public void endEdit(EditCommand c) {
//        this.service.setTempEditCommand(c);
//
//        for (Zone zone : this.service.getZones()) {
//            String clickFuncReferenceEditZone = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditZone" + zone.getID();
//            zone.getPolygon().off("click", clickFuncReferenceEditZone);
//        }
//        for (org.pinguweb.frontend.mapObjects.Route route : this.service.getRoutes()) {
//            String clickFuncReferenceEditRoute = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditRoute" + route.getID();
//            route.getPolygon().off("click", clickFuncReferenceEditRoute);
//        }
//        for (Storage storage : this.service.getStorages()) {
//            String clickFuncReferenceEditStorage = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncEditStorage" + storage.getID();
//            storage.getMarkerObj().off("click", clickFuncReferenceEditStorage);
//        }
//
//        Notification notification = new Notification("Edición realizada exitosamente", 3000);
//        notification.open();
//    }
//
//    public void startDelete() {
//        for (Need need : this.service.getNeeds()) {
//            String clickFuncReferenceDeleteMarker = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteMarker" + need.getID();
//            this.service.getReg().execJs(clickFuncReferenceDeleteMarker + "=e => document.getElementById('" + this.service.getID() + "').$server.removeMarker('" + need.getID() + "')");
//            need.getMarkerObj().on("click", clickFuncReferenceDeleteMarker);
//        }
//        for (Zone zone : this.service.getZones()) {
//            String clickFuncReferenceDeleteZone = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteZone" + zone.getID();
//            this.service.getReg().execJs(clickFuncReferenceDeleteZone + "=e => document.getElementById('" + this.service.getID() + "').$server.removePolygon('" + zone.getID() + "') ");
//            zone.getPolygon().on("click", clickFuncReferenceDeleteZone);
//        }
//        for (org.pinguweb.frontend.mapObjects.Route route : this.service.getRoutes()) {
//            String clickFuncReferenceDeleteRoute = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteRoute" + route.getID();
//            this.service.getReg().execJs(clickFuncReferenceDeleteRoute + "=e => document.getElementById('" + this.service.getID() + "').$server.removeRoute('" + route.getID() + "') ");
//            route.getPolygon().on("click", clickFuncReferenceDeleteRoute);
//        }
//        for (Storage storage : this.service.getStorages()) {
//            String clickFuncReferenceDeleteStorage = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteStorage" + storage.getID();
//            this.service.getReg().execJs(clickFuncReferenceDeleteStorage + "=e => document.getElementById('" + this.service.getID() + "').$server.removeStorage('" + storage.getID() + "') ");
//            storage.getMarkerObj().on("click", clickFuncReferenceDeleteStorage);
//        }
//    }
//
//    public void endDelete(DeleteCommand c) {
//        this.service.setTempDeleteCommand(c);
//        for (Zone zone : this.service.getZones()) {
//            String clickFuncReferenceDeleteZone = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteZone" + zone.getID();
//            zone.getPolygon().off("click", clickFuncReferenceDeleteZone);
//        }
//        for (org.pinguweb.frontend.mapObjects.Route route : this.service.getRoutes()) {
//            String clickFuncReferenceDeleteRoute = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteRoute" + route.getID();
//            route.getPolygon().off("click", clickFuncReferenceDeleteRoute);
//        }
//        for (Storage storage : this.service.getStorages()) {
//            String clickFuncReferenceDeleteStorage = this.service.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteStorage" + storage.getID();
//            storage.getMarkerObj().off("click", clickFuncReferenceDeleteStorage);
//        }
//    }
//
//    public void editStorage(Storage storage) {
//        log.debug("Almacen editado");
//        storage.updateToServer();
//        service.getStorages().stream().filter(s -> Objects.equals(s.getID(), storage.getID())).findFirst().ifPresent(s -> {
//            service.getStorages().remove(s);
//            service.getStorages().add(storage);
//        });
//        service.updateStorage(storage);
//    }

}
