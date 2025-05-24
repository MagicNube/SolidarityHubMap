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
import org.pingu.web.BackendObservableService.observableList.Observer;
import org.pingu.web.BackendObservableService.observableList.ObserverChange;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Commands.Command;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.Map;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.ClickedElement;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.DialogsNames;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ClickedEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.CreationEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.LoadEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ShowEvent;
import org.pinguweb.frontend.interfaceBuilders.Directors.MapBuilderDirector;
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
            startZoneConstruction();
        }
        else if (event.getPayload() == ClickedElement.ROUTE_POINT){
            startRouteConstruction();
        }
        else if (event.getPayload() == ClickedElement.STORAGE){
            startStorageConstruction();
        }

        log.info(map.getMapContainer().getClassNames().toString());
    }

    @Override
    public void update(ObserverChange change) {
        ui.access(() -> this.mediator.publish(new LoadEvent<>()));
        log.info("Mapa actualizado");
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
        ShowEvent<Tuple<Double, Double>> event = new ShowEvent<Tuple<Double, Double>>(EventType.SHOW_DIALOG, coords, DialogsNames.STORAGE);
        mediator.publish(event);
        map.getMap().off("click", clickFuncReferenceCreateStorage);
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