package org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.InterfaceComponent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapColleagues.*;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.ButtonEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.GenericEvent;
import org.pinguweb.frontend.interfaceBuilders.CustomUIComponents.Map.MapEvents.LoadEvent;
import org.pinguweb.frontend.mapObjects.*;
import org.pinguweb.frontend.utils.Mediador.Colleague;
import org.pinguweb.frontend.utils.Mediador.Event;
import org.pinguweb.frontend.utils.Mediador.EventType;
import org.pinguweb.frontend.utils.Mediador.Mediator;
import org.pinguweb.frontend.view.MapView;
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

    @Setter
    private MapState state;
    private VerticalLayout component;

    private HashSet<Storage> storages = new HashSet<>();
    private HashSet<Need> needs = new HashSet<>();
    private HashSet<Zone> zones = new HashSet<>();
    private HashSet<Route> routes = new HashSet<>();
    private HashMap<Integer, List<RoutePoint>> routePoints = new HashMap<>();

    private HashMap<Tuple<Double, Double>, ZoneMarker> zoneMarkers = new HashMap<>();
    private List<Tuple<Double, Double>> zoneMarkerPoints = new ArrayList<>();
    private Tuple<Double, Double> zoneMarkerStartingPoint;

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
         zoneMarkers = new HashMap<>();
         zoneMarkerPoints = new ArrayList<>();
    }

    public void loadView() {
        this.component = new VerticalLayout();
        this.component.setSizeFull();

        init();

        this.reg = new LDefaultComponentManagementRegistry(this.component);
        MapContainer mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.map = mapContainer.getlMap();
        LLayer<LTileLayer> layer = LTileLayer.createDefaultForOpenStreetMapTileServer(reg);
        this.map.addLayer(layer);
        this.map.locate(new LMapLocateOptions().withSetView(true).withMaxZoom(16));

        component.add(mapContainer);
        component.add(new MapButtons(this).generateButtonRow());

        new MapService(this);
        new MapDialogs(this);
        new MapBuild(this);

        state = MapState.IDLE;
        publish(new LoadEvent<>());
        publish(new ButtonEvent<>(EventType.ENABLE_BUTTONS, null));
    }

    @Override
    public void subscribe(EventType eventType, Colleague colleague) {
        suscribers.add(new Tuple<>(colleague, eventType));
    }

    @Override
    public <T> void publish(Event<T> event) {
        log.info("Map received event of type: {}", event.getType());
        suscribers.stream().filter(x -> x._2() == event.getType())
                            .forEach(x -> x._1().receive(event));
    }
}
