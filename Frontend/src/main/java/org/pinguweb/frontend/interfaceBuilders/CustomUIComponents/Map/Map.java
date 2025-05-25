package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.*;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapShow;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapButtons;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapDialogs;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.MapService;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ButtonEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.LoadEvent;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.utils.Mediador.Colleague;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;
import org.pinguweb.frontend.utils.Mediador.Mediator;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.layer.LLayer;
import software.xdev.vaadin.maps.leaflet.layer.LLayerGroup;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.map.LMapLocateOptions;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Getter
@SuperBuilder
public class Map extends InterfaceComponent implements Mediator {
    private final List<Tuple<Colleague, EventType>> suscribers = new ArrayList<>();

    private LDefaultComponentManagementRegistry reg;
    private LMap map;
    private MapContainer mapContainer;
    private VerticalLayout component;
    private Div actionBanner;

    private HashSet<Storage> storages = new HashSet<>();
    private HashSet<Need> needs = new HashSet<>();
    private HashSet<Zone> zones = new HashSet<>();
    private HashSet<Route> routes = new HashSet<>();
    private HashMap<Integer, List<RoutePoint>> routePoints = new HashMap<>();
    private List<RoutePoint> newRoutePoints= new ArrayList<>();

    private HashMap<Tuple<Double, Double>, ZoneMarker> zoneMarkers = new HashMap<>();
    private List<Tuple<Double, Double>> zoneMarkerPoints = new ArrayList<>();

    @Setter
    private LLayerGroup lLayerGroupZones;
    @Setter
    private LLayerGroup lLayerGroupNeeds;
    @Setter
    private LLayerGroup lLayerGroupRoutes;
    @Setter
    private LLayerGroup lLayerGroupStorages;

    private void init(){
         storages = new HashSet<>();
         needs = new HashSet<>();
         zones = new HashSet<>();
         routes = new HashSet<>();
         routePoints = new HashMap<>();
         newRoutePoints = new ArrayList<>();
         zoneMarkers = new HashMap<>();
         zoneMarkerPoints = new ArrayList<>();
    }

    public void loadView() {
        this.component = new VerticalLayout();
        this.component.setSizeFull();

        init();

        this.reg = new LDefaultComponentManagementRegistry(this.component);
        mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.map = mapContainer.getlMap();
        LLayer<LTileLayer> layer = LTileLayer.createDefaultForOpenStreetMapTileServer(reg);
        this.map.addLayer(layer);
        this.map.locate(new LMapLocateOptions().withSetView(true).withMaxZoom(16));

        component.add(mapContainer);
        component.add(new MapButtons(this).generateButtonRow());

        new MapService(this);
        new MapDialogs(this);
        new MapShow(this);

        initializeActionBanner();


        publish(new LoadEvent<>());
        publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS, null));

    }

    private void initializeActionBanner(){
        this.actionBanner = new Div();
        this.actionBanner.setId("action-banner");

        this.actionBanner.setText("Modo: Navegación");

        // Estilos para posicionar y centrar el banner
        this.actionBanner.getStyle().set("position", "absolute");
        this.actionBanner.getStyle().set("top", "10px");
        this.actionBanner.getStyle().set("left", "50%");
        this.actionBanner.getStyle().set("transform", "translateX(-50%)");
        this.actionBanner.getStyle().set("text-align", "center");
        this.actionBanner.getStyle().set("background-color", "rgba(0, 0, 0, 0.7)");
        this.actionBanner.getStyle().set("color", "white");
        this.actionBanner.getStyle().set("font-weight", "bold");
        this.actionBanner.getStyle().set("z-index", "1000");
        this.actionBanner.getStyle().set("padding", "8px 15px");

        this.component.add(this.actionBanner);
    }



    @Override
    public void subscribe(EventType eventType, Colleague colleague) {
        suscribers.add(new Tuple<>(colleague, eventType));
    }

    @Override
    public <T> void publish(Event<T> event) {
        suscribers.stream().filter(x -> x._2() == event.getType())
                            .forEach(x -> x._1().receive(event));
    }
}
