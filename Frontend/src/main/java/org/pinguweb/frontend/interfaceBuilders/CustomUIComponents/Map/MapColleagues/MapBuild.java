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
        map.getStorages().stream().filter(s -> Objects.equals(s.getID(), storage.getID())).findFirst().ifPresent(s -> {
            map.getStorages().remove(s);
        });

        map.getStorages().add(storage);
        map.getLLayerGroupStorages().addLayer(storage.getMarkerObj());
        this.map.getMap().addLayer(map.getLLayerGroupStorages());

        map.getMap().off("click", clickFuncReferenceCreateStorage);
        map.getMapContainer().removeClassName("cursor-crear");
    }

    public void showZone(Zone zone, CreateZoneCommand c) {
        for (ZoneMarker zoneMarker : map.getZoneMarkers().values()) {
            zoneMarker.removeFromMap(map.getMap());
        }

        map.getMap().off("click", clickFuncReferenceCreateZone);
        map.getZoneMarkers().clear();
        map.getZoneMarkerPoints().clear();

        zone.addToMap(this.map.getMap());
        map.getZones().add(zone);
        map.getLLayerGroupZones().addLayer(zone.getPolygon());
        this.map.getMap().addLayer(map.getLLayerGroupZones());
        map.getMapContainer().removeClassName("cursor-crear");
    }

    public void showZoneMarker(double lat, double lng) {
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

        map.getMapContainer().removeClassName("cursor-crear");
    }

//    public void editZone(Zone zone) {
//        log.debug("Zona editada");
//        zone.updateToServer();
//        service.updateZone(zone);
//    }


    public void showRoute(Route ruta, List<RoutePoint> points, CreateRouteCommand c) {
        map.getMap().off("click", clickFuncReferenceCreateRoute);
        List<Integer> pointsID = new ArrayList<>();
        for (RoutePoint routePoint : points) {
            pointsID.add(routePoint.pushToServer());
        }

        if (c != null) {c.setPoints(points);}

        ruta.setPointsID(pointsID);

        if (c != null) {c.setRoute(ruta);}

        map.getRoutes().stream().filter(r -> Objects.equals(r.getID(), ruta.getID())).findFirst().ifPresent(r -> {
            map.getRoutes().remove(r);
            map.getRoutes().add(ruta);
        });

        points.get(0).addToMap(map.getMap());
        points.get(points.size()-1).addToMap(map.getMap());

        map.getRoutes().add(ruta);
        map.getLLayerGroupRoutes().addLayer(ruta.getPolygon());
        map.getLLayerGroupRoutes().addLayer(points.get(0).getMarkerObj());
        map.getLLayerGroupRoutes().addLayer(points.get(points.size() - 1).getMarkerObj());
        points.get(0).getMarkerObj().bindPopup("Ruta: " + ruta.getName());
        points.get(points.size() - 1).getMarkerObj().bindPopup("Ruta: " + ruta.getName());
        this.map.getMap().addLayer(map.getLLayerGroupRoutes());
        map.getMapContainer().removeClassName("cursor-crear");
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
        map.getMapContainer().removeClassName("cursor-crear");
    }
}
