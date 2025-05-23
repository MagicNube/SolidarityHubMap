package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.*;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.mapObjects.factories.*;
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

    public MapBuild(Map map) {
        super(map);
        this.map = map;
        clickFuncReferenceCreateZone = map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateZone";
        clickFuncReferenceCreateRoute = map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateRoute";
        clickFuncReferenceCreateRoute = map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateStorage";

    }

    @Override
    public void register() {
        mediator.subscribe(EventType.SHOW, this);
    }

    @Override
    public <T> void receive(Event<T> event) {
        if (Objects.requireNonNull(event.getType()) == EventType.SHOW) {
            if (event.getPayload() instanceof Storage) {
                showStorage((Storage) event.getPayload(), (CreateStorageCommand) event.getCommand());
            } else if (event.getPayload() instanceof Zone) {
                showZone((Zone) event.getPayload(), (CreateZoneCommand) event.getCommand());
            } else if (event.getPayload() instanceof Route) {
                showRoute((Route) event.getPayload(), ((CreationEvent<T>) event).getExtraData(), (CreateRouteCommand) event.getCommand());
            } else if (event instanceof CreationEvent<T>){
                CreationEvent<T> e = (CreationEvent<T>) event;
                if (e.getElement() == ClickedElement.ROUTE_POINT){
                    Tuple<Double, Double> coords = (Tuple<Double, Double>) e.getPayload();
                    showRoutePoint(coords._1(), coords._2());
                }
                else if (e.getElement() == ClickedElement.ZONE_MARKER){
                    Tuple<Double, Double> coords = (Tuple<Double, Double>) e.getPayload();
                    showZoneMarker(coords._1(), coords._2());
                }
            }
        }
    }

    public void showStorage(Storage storage, CreateStorageCommand c){
        if (c != null ) {c.setStorage(storage);}
//        int tempId = storage.pushToServer();
//        storage.setID(tempId);
        map.getStorages().stream().filter(s -> Objects.equals(s.getID(), storage.getID())).findFirst().ifPresent(s -> {
            map.getStorages().remove(s);
        });

        map.getMap().off("click", clickFuncReferenceCreateStorage);
//
//        Notification notification = new Notification("Almacén creado exitosamente", 3000);
//        notification.open();
    }


    public void showZone(Zone zone, CreateZoneCommand c) {
        log.debug("Zona terminada");
//        Zone zona = createZone(zone);
//
//        int newID = zona.pushToServer();
        map.getZones().stream().filter(z -> z.getID() == zone.getID()).findFirst().ifPresent(z -> {
//            z.setID(newID);
        });

        for (ZoneMarker zoneMarker : map.getZoneMarkers().values()) {
            zoneMarker.removeFromMap(map.getMap());
        }

        map.getMap().off("click", clickFuncReferenceCreateZone);
        map.getZoneMarkers().clear();
        map.getZoneMarkerPoints().clear();
//        if (c != null) {c.setZone(zona);}
    }

    public ZoneMarker showZoneMarker(double lat, double lng) {
        ZoneMarkerFactory zoneMarkerFactory = new ZoneMarkerFactory();
        ZoneMarker zoneMarker = (ZoneMarker) zoneMarkerFactory.createMapObject(this.map.getReg(), lat, lng);

        String clickFuncReferenceDragStart = this.map.getMap().clientComponentJsAccessor() + ".myClickFuncDragStart" + zoneMarker.getID();
        String clickFuncReferenceDragEnd = this.map.getMap().clientComponentJsAccessor() + ".myClickFuncDragEnd" + zoneMarker.getID();

        this.map.getReg().execJs(clickFuncReferenceDragStart + "=e => document.getElementById('" + MapView.getMapId() + "').$server.routePointStart(e.target.getLatLng())");
        this.map.getReg().execJs(clickFuncReferenceDragEnd + "=e => document.getElementById('" + MapView.getMapId() + "').$server.routePointEnd(e.target.getLatLng())");

        zoneMarker.getMarkerObj().on("dragstart", clickFuncReferenceDragStart);
        zoneMarker.getMarkerObj().on("dragend", clickFuncReferenceDragEnd);

        zoneMarker.addToMap(this.map.getMap());
        Tuple<Double, Double> coord = new Tuple<>(lat, lng);
        map.getZoneMarkerPoints().add(coord);
        map.getZoneMarkers().put(coord, zoneMarker);

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


    public void showRoute(Route ruta, List<RoutePoint> points, CreateRouteCommand c) {
        map.getMap().off("click", clickFuncReferenceCreateRoute);
        log.debug("Ruta terminada");
        List<Integer> pointsID = new ArrayList<>();
        for (RoutePoint routePoint : points) {
            pointsID.add(routePoint.pushToServer());
        }

        if (c != null) {c.setPoints(points);}

        ruta.setPointsID(pointsID);
//        int tempID = ruta.pushToServer();

        if (c != null) {c.setRoute(ruta);}

        map.getRoutes().stream().filter(r -> r.getID() == ruta.getID()).findFirst().ifPresent(r -> {
            map.getRoutes().remove(r);
            map.getRoutes().add(ruta);
        });
//        map.getRoutePoints().put(tempID, new ArrayList<>(points));

        for (int i = points.size() - 2; i > 0; i--) {
            RoutePoint routePoint = points.get(i);
            routePoint.removeFromMap(map.getMap());
        }
    }

    public void showRoutePoint(double lat, double lng) {
        RoutePoint routePoint = (RoutePoint) new RoutePointFactory().createMapObject(this.map.getReg(), lat, lng);

        String clickFuncReferenceDragStart = this.map.getMap().clientComponentJsAccessor() + ".myClickFuncDragStart" + routePoint.getID();
        String clickFuncReferenceDragEnd = this.map.getMap().clientComponentJsAccessor() + ".myClickFuncDragEnd" + routePoint.getID();

        this.map.getReg().execJs(clickFuncReferenceDragStart + "=e => document.getElementById('" + MapView.getMapId() + "').$server.routePointStart(e.target.getLatLng())");
        this.map.getReg().execJs(clickFuncReferenceDragEnd + "=e => document.getElementById('" + MapView.getMapId() + "').$server.routePointEnd(e.target.getLatLng())");

        routePoint.getMarkerObj().on("dragstart", clickFuncReferenceDragStart);
        routePoint.getMarkerObj().on("dragend", clickFuncReferenceDragEnd);

        routePoint.addToMap(this.map.getMap());
        map.getNewRoutePoints().add(routePoint);
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
