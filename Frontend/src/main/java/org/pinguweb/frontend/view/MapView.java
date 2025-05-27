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
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.DeleteCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.ConcreteCommands.EditCommand;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.enums.DialogsNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.GenericEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.LoadEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ShowEvent;
import org.pinguweb.frontend.interfaceBuilders.Directors.MapBuilderDirector;
import org.pinguweb.frontend.mapObjects.Storage;
import org.pinguweb.frontend.mapObjects.Zone;
import org.pinguweb.frontend.services.BackendDTOService;
import org.pinguweb.frontend.utils.Mediador.Colleague;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;
import org.pinguweb.frontend.utils.Mediador.Mediator;
import org.yaml.snakeyaml.util.Tuple;

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

    Command lastCommand;
    UI ui;
    DeleteCommand deleteCommand;
    EditCommand editCommand;

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
        clickFuncReferenceCreateStorage = map.getMap().clientComponentJsAccessor() + ".myClickFuncCreateStorage";

        register();
    }

    @Override
    public void register() {
        mediator.subscribe(EventType.REQUEST_CLICK, this);
    }

    @Override
    public <T> void receive(Event<T> event) {
        lastCommand = event.getCommand();
        if (event.getPayload() == ClickedElement.ZONE){
            this.mediator.publish(new GenericEvent<>(EventType.CHANGE_BANNER, "Crear zona", null));
            startZoneConstruction();
        }
        else if (event.getPayload() == ClickedElement.ROUTE_POINT){
            this.mediator.publish(new GenericEvent<>(EventType.CHANGE_BANNER, "Crear ruta", null));
            startRouteConstruction();
        }
        else if (event.getPayload() == ClickedElement.STORAGE){
            this.mediator.publish(new GenericEvent<>(EventType.CHANGE_BANNER, "Crear almacén", null));
            startStorageConstruction();
        }
        else if (event.getPayload() == ClickedElement.DELETE && event.getCommand() instanceof DeleteCommand){
            if (((DeleteCommand) event.getCommand()).isWorking()){
                endDelete();
                this.mediator.publish(new GenericEvent<>(EventType.CHANGE_BANNER, "Navegación", null));
            }
            else{
                startDelete();
                this.mediator.publish(new GenericEvent<>(EventType.CHANGE_BANNER, "Borrar", null));
                deleteCommand = (DeleteCommand) event.getCommand();
            }
        }
        else if (event.getPayload() == ClickedElement.EDIT && event.getCommand() instanceof EditCommand){
            if (((EditCommand) event.getCommand()).isWorking()){
                endEdit();
                this.mediator.publish(new GenericEvent<>(EventType.CHANGE_BANNER, "Navegación", null));
            }
            else{
                startEdit();
                this.mediator.publish(new GenericEvent<>(EventType.CHANGE_BANNER, "Editar", null));
                editCommand = (EditCommand) event.getCommand();
            }
        }
    }

    @Override
    public void update(ObserverChange change) {
        ui.access(() -> this.mediator.publish(new LoadEvent<>()));
        log.info("Mapa actualizado");
    }

    public void startDelete() {
        map.getMapContainer().getClassNames().add("cursor-borrar");
        for (Zone zone : map.getZones()) {
            String clickFuncReferenceDeleteZone = map.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteZone" + zone.getID();
            map.getReg().execJs(clickFuncReferenceDeleteZone + "=e => document.getElementById('" + MapView.getMapId() + "').$server.removePolygon('" + zone.getID() + "') ");
            zone.getPolygon().on("click", clickFuncReferenceDeleteZone);
        }
        for (org.pinguweb.frontend.mapObjects.Route route : map.getRoutes()) {
            String clickFuncReferenceDeleteRoute = map.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteRoute" + route.getID();
            map.getReg().execJs(clickFuncReferenceDeleteRoute + "=e => document.getElementById('" +  MapView.getMapId() + "').$server.removeRoute('" + route.getID() + "') ");
            route.getPolygon().on("click", clickFuncReferenceDeleteRoute);
        }
        for (Storage storage : map.getStorages()) {
            String clickFuncReferenceDeleteStorage = map.getMap().clientComponentJsAccessor() + ".myClickFuncDeleteStorage" + storage.getID();
            map.getReg().execJs(clickFuncReferenceDeleteStorage + "=e => document.getElementById('" +  MapView.getMapId() + "').$server.removeStorage('" + storage.getID() + "') ");
            storage.getMarkerObj().on("click", clickFuncReferenceDeleteStorage);
        }
    }

    public void endDelete() {
        map.getMapContainer().getClassNames().clear();
        for (Zone zone : map.getZones()) {
            zone.getPolygon().off("click");
        }
        for (org.pinguweb.frontend.mapObjects.Route route : map.getRoutes()) {
            route.getPolygon().off("click");
        }
        for (Storage storage : map.getStorages()) {
            storage.getMarkerObj().off("click");
        }
    }

    public void startEdit() {
        map.getMapContainer().getClassNames().add("cursor-editar");
        for (Zone zone : map.getZones()) {
            String clickFuncReferenceEditZone = map.getMap().clientComponentJsAccessor() + ".myClickFuncEditZone" + zone.getID();
            map.getReg().execJs(clickFuncReferenceEditZone + "=e => document.getElementById('" + MapView.getMapId() + "').$server.editPolygon('" + zone.getID() + "') ");
            zone.getPolygon().on("click", clickFuncReferenceEditZone);
        }
        for (org.pinguweb.frontend.mapObjects.Route route : map.getRoutes()) {
            String clickFuncReferenceEditRoute = map.getMap().clientComponentJsAccessor() + ".myClickFuncEditRoute" + route.getID();
            map.getReg().execJs(clickFuncReferenceEditRoute + "=e => document.getElementById('" + MapView.getMapId() + "').$server.editRoute('" + route.getID() + "') ");
            route.getPolygon().on("click", clickFuncReferenceEditRoute);
        }
        for (Storage storage : map.getStorages()) {
            String clickFuncReferenceEditStorage = map.getMap().clientComponentJsAccessor() + ".myClickFuncEditStorage" + storage.getID();
            map.getReg().execJs(clickFuncReferenceEditStorage + "=e => document.getElementById('" + MapView.getMapId() + "').$server.editStorage('" + storage.getID() + "') ");
            storage.getMarkerObj().on("click", clickFuncReferenceEditStorage);
        }
    }

    public void endEdit() {
        map.getMapContainer().getClassNames().clear();
        for (Zone zone : map.getZones()) {
            String clickFuncReferenceEditZone = map.getMap().clientComponentJsAccessor() + ".myClickFuncEditZone" + zone.getID();
            zone.getPolygon().off("click", clickFuncReferenceEditZone);
        }
        for (org.pinguweb.frontend.mapObjects.Route route : map.getRoutes()) {
            String clickFuncReferenceEditRoute = map.getMap().clientComponentJsAccessor() + ".myClickFuncEditRoute" + route.getID();
            route.getPolygon().off("click", clickFuncReferenceEditRoute);
        }
        for (Storage storage : map.getStorages()) {
            String clickFuncReferenceEditStorage = map.getMap().clientComponentJsAccessor() + ".myClickFuncEditStorage" + storage.getID();
            storage.getMarkerObj().off("click", clickFuncReferenceEditStorage);
        }
    }

    public void startZoneConstruction() {
        map.getMapContainer().getClassNames().add("cursor-crear");
        map.getReg().execJs(clickFuncReferenceCreateZone + "=e => document.getElementById('" + mapId + "').$server.mapZona(e.latlng)");
        map.getMap().on("click", clickFuncReferenceCreateZone);
    }

    public void startRouteConstruction() {
        map.getMapContainer().getClassNames().add("cursor-crear");
        map.getReg().execJs(clickFuncReferenceCreateRoute + "=e => document.getElementById('" + mapId + "').$server.mapRoute(e.latlng)");
        map.getMap().on("click", clickFuncReferenceCreateRoute);
    }

    public void startStorageConstruction() {
        map.getMapContainer().getClassNames().add("cursor-crear");
        map.getReg().execJs(clickFuncReferenceCreateStorage + "=e => document.getElementById('" + mapId + "').$server.mapStorage(e.latlng)");
        map.getMap().on("click", clickFuncReferenceCreateStorage);
    }

    @ClientCallable
    public void mapZona(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        Tuple<Double, Double> coords = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));
        CreationEvent<Tuple<Double, Double>> event = new CreationEvent<Tuple<Double, Double>>(EventType.SHOW, coords, lastCommand, null);
        event.setElement(ClickedElement.ZONE_MARKER);
        mediator.publish(event);
    }

    @ClientCallable
    public void mapRoute(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }
        Tuple<Double, Double> coords = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));
        CreationEvent<Tuple<Double, Double>> event = new CreationEvent<Tuple<Double, Double>>(EventType.SHOW, coords, lastCommand, null);
        event.setElement(ClickedElement.ROUTE_POINT);
        mediator.publish(event);
    }

    @ClientCallable
    public void mapStorage(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        Tuple<Double, Double> coords = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));
        ShowEvent<Tuple<Double, Double>> event = new ShowEvent<Tuple<Double, Double>>(EventType.SHOW_DIALOG, coords, DialogsNames.STORAGE, lastCommand);
        mediator.publish(event);
        map.getMap().off("click", clickFuncReferenceCreateStorage);
    }

    @ClientCallable
    public void removePolygon(String ID) {
        endDelete();
        ZoneDTO zone = new ZoneDTO();
        zone.setID(Integer.parseInt(ID));
        deleteCommand.endExecution();
        mediator.publish(new GenericEvent<>(EventType.DELETE, zone, null));
    }

    @ClientCallable
    public void removeRoute(String ID) {
        endDelete();
        RouteDTO route = new RouteDTO();
        route.setID(Integer.parseInt(ID));
        deleteCommand.endExecution();
        mediator.publish(new GenericEvent<>(EventType.DELETE, route, null));
    }

    @ClientCallable
    public void removeStorage(String ID) {
        endDelete();
        StorageDTO storage = new StorageDTO();
        storage.setID(Integer.parseInt(ID));
        deleteCommand.endExecution();
        mediator.publish(new GenericEvent<>(EventType.DELETE, storage, null));
    }

    @ClientCallable
    public void editPolygon(String ID) {
        endEdit();
        editCommand.endExecution();
        mediator.publish(new ShowEvent<>(EventType.SHOW_EDIT, ID, DialogsNames.ZONE, editCommand));
    }

    @ClientCallable
    public void editRoute(String ID) {
        endEdit();
        editCommand.endExecution();
        mediator.publish(new ShowEvent<>(EventType.SHOW_EDIT, ID, DialogsNames.ROUTE, editCommand));
    }

    @ClientCallable
    public void editStorage(String ID) {
        endEdit();
        editCommand.endExecution();
        mediator.publish(new ShowEvent<>(EventType.SHOW_EDIT, ID, DialogsNames.STORAGE, editCommand));
    }
}