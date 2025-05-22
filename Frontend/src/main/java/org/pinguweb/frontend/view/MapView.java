package org.pinguweb.frontend.view;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pingu.domain.DTO.RouteDTO;
import org.pingu.domain.DTO.StorageDTO;
import org.pingu.domain.DTO.ZoneDTO;
import org.pingu.web.BackendObservableService.observableList.Observer;
import org.pingu.web.BackendObservableService.observableList.ObserverChange;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.CreateStorageCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapState;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ClickedEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.LoadEvent;
import org.pinguweb.frontend.interfaceBuilders.Directors.MapBuilderDirector;
import org.pinguweb.frontend.mapObjects.RoutePoint;
import org.pinguweb.frontend.mapObjects.ZoneMarker;
import org.pinguweb.frontend.services.BackendDTOService;
import org.pinguweb.frontend.utils.Mediador.Colleague;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;
import org.pinguweb.frontend.utils.Mediador.Mediator;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.controls.LControlLayers;
import software.xdev.vaadin.maps.leaflet.controls.LControlLayersOptions;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.LLayerGroup;

import java.util.LinkedHashMap;
import java.util.Objects;

@Slf4j
@Route("map")
@PageTitle("Visor del mapa")
public class MapView extends HorizontalLayout implements Observer, Colleague {

    @Getter
    private static String mapId = "MapView";

    @Setter
    private Map map;
    @Setter
    private Mediator mediator;

    private String clickFuncReferenceCreateZone;
    private String clickFuncReferenceCreateRoute;
    private String clickFuncReferenceCreateStorage;

    UI ui;

    public MapView() {
        this.setSizeFull();
        this.setId(mapId);

        BackendDTOService.GetInstancia().getNeedList().attach(this,ObserverChange.ADD_ALL);
        BackendDTOService.GetInstancia().getZoneList().attach(this,ObserverChange.ADD_ALL);
        BackendDTOService.GetInstancia().getStorageList().attach(this,ObserverChange.ADD_ALL);
        BackendDTOService.GetInstancia().getRouteList().attach(this,ObserverChange.ADD_ALL);
        BackendDTOService.GetInstancia().getRoutePointList().attach(this,ObserverChange.ADD_ALL);
        BackendDTOService.GetInstancia().getCatastropheList().attach(this, ObserverChange.ADD_ALL);

        MapBuilderDirector director = new MapBuilderDirector();
        this.add(director.createFullMap(this));

        this.ui = UI.getCurrent();
        if (ui == null) {
            log.warn("UI is null, cannot update UI components.");
        }

        clickFuncReferenceCreateZone = map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateZone";
        clickFuncReferenceCreateRoute = map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateRoute";
        clickFuncReferenceCreateRoute = map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateStorage";

        register();
    }


    @Override
    public void register() {

    }

    @Override
    public <T> void receive(Event<T> event) {

    }

    @Override
    public void update(ObserverChange change) {
        ui.access(() -> this.mediator.publish(new LoadEvent<>()));
        log.info("Mapa actualizado");
    }

    public void startZoneConstruction() {
        map.getReg().execJs(clickFuncReferenceCreateZone + "=e => document.getElementById('" + mapId + "').$server.mapZona(e.latlng)");
        map.getMap().on("click", clickFuncReferenceCreateZone);
    }

    public void startRouteConstruction() {
        map.getReg().execJs(clickFuncReferenceCreateRoute + "=e => document.getElementById('" + mapId + "').$server.mapRoute(e.latlng)");
        map.getMap().on("click", clickFuncReferenceCreateRoute);
    }

    public void createStorage(StorageDTO storageDTO, CreateStorageCommand c) {
        map.getReg().execJs(clickFuncReferenceCreateStorage + "=e => document.getElementById('" + mapId + "').$server.mapStorage(e.latlng)");
        map.getMap().on("click", clickFuncReferenceCreateStorage);
    }

    @ClientCallable
    public void mapZona(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        mediator.publish(new ClickedEvent<>(EventType.BUILD, obj, ClickedElement.ZONE_MARKER));
    }

    @ClientCallable
    public void mapRoute(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        mediator.publish(new ClickedEvent<>(EventType.BUILD, obj, ClickedElement.ROUTE_POINT));
    }

    @ClientCallable
    public void zoneMarkerStart(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        mediator.publish(new ClickedEvent<>(EventType.BUILD, obj, ClickedElement.ZONE_MARKER_START));
    }

    @ClientCallable
    public void zoneMarkerEnd(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        mediator.publish(new ClickedEvent<>(EventType.BUILD, obj, ClickedElement.ZONE));
    }

    @ClientCallable
    public void routePointStart(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        mediator.publish(new ClickedEvent<>(EventType.BUILD, obj, ClickedElement.ROUTE_POINT_START));
    }

    @ClientCallable
    public void routePointEnd(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        mediator.publish(new ClickedEvent<>(EventType.BUILD, obj, ClickedElement.ROUTE_POINT_END));
    }

    @ClientCallable
    public void mapStorage(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        mediator.publish(new ClickedEvent<>(EventType.BUILD, obj, ClickedElement.STORAGE));
    }

//    @ClientCallable
//    public void removePolygon(String ID) {
//        System.out.println("removePolygon: " + ID);
//        controller.deleteZone(Integer.parseInt(ID));
//    }
//
//    @ClientCallable
//    public void removeRoute(String ID) {
//        System.out.println("removeRoute: " + ID);
//        controller.deleteRoute(Integer.parseInt(ID));
//    }
//
//    @ClientCallable
//    public void removeStorage(String ID) {
//        System.out.println("removeStorage: " + ID);
//        controller.deleteStorage(Integer.parseInt(ID));
//    }
//
//    @ClientCallable
//    public void editPolygon(String ID) {
//        System.out.println("editPolygon: " + ID);
//        mapDialogs.editDialogZone(ID, controller.getTempEditCommand());
//    }
//
//    @ClientCallable
//    public void editRoute(String ID) {
//        System.out.println("editRoute: " + ID);
//        mapDialogs.editDialogRoute(ID, controller.getTempEditCommand());
//    }
//
//    @ClientCallable
//    public void editStorage(String ID) {
//        System.out.println("editStorage: " + ID);
//        mapDialogs.editDialogStorage(ID, controller.getTempEditCommand());
//    }
}