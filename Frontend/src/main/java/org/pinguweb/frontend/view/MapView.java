package org.pinguweb.frontend.view;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import org.pinguweb.frontend.services.map.MapService;
import org.pinguweb.frontend.services.map.MapTypes;
import org.yaml.snakeyaml.util.Tuple;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.layer.ui.LMarker;
import software.xdev.vaadin.maps.leaflet.map.LMapLocateOptions;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.pinguweb.frontend.services.map.MapTypes.TAREA;
import static org.pinguweb.frontend.services.map.MapTypes.ZONA;

@Route("map")
@PageTitle("Visor del mapa")
public class MapView extends HorizontalLayout {

    MapService controller;
    private final String ID = "mapa";
    private final LMap map;
    private LComponentManagementRegistry reg;
    private final MapContainer mapContainer;
    private final Object lock = new Object();
    private UI ui;
    private final Button tarea = new Button("Tarea");
    private final Button zona = new Button("Zona");
    private final HashMap<Tuple<Double, Double>, LMarker> zoneMarkers = new HashMap<>();
    private final List<Tuple<Double, Double>> zoneMarkerPoints = new ArrayList<>();
    private Tuple<Double, Double> zoneMarkerStartingPoint;
    private final String clickFuncReference;

    public MapView() {
        this.setId(ID);
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());

        VerticalLayout MapVerticalLayout = new VerticalLayout();
        HorizontalLayout ButtonLayout = new HorizontalLayout();

        this.add(MapVerticalLayout);

        LComponentManagementRegistry reg = new LDefaultComponentManagementRegistry(this);
        mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.map = mapContainer.getlMap();
        this.map.addLayer(LTileLayer.createDefaultForOpenStreetMapTileServer(reg));
        clickFuncReference = map.clientComponentJsAccessor() + ".myCoolClickFunc";
        reg.execJs(clickFuncReference + "=e => document.getElementById('" + ID + "').$server.mapZona(e.latlng)");

        this.map.locate(new LMapLocateOptions().withSetView(true));

        MapVerticalLayout.add(mapContainer);
        MapVerticalLayout.add(ButtonLayout);
        tarea.isDisableOnClick();
        ButtonLayout.add(tarea, zona);

        this.controller = new MapService();
        this.controller.setReg(reg);
        this.controller.setMap(map);
        this.controller.setID(ID);
        this.controller.load();

        tarea.addClickListener(e -> click(TAREA, tarea));
        zona.addClickListener(e -> click(ZONA, zona));

    }

    public void click(MapTypes Action, Button button ) {
        switch (Action) {
            case TAREA:
                this.mapContainer.addClassName("map_action");
                this.controller.crearDialogoTarea();
                this.map.once("click", "e => document.getElementById('" + ID + "').$server.mapTarea(e.latlng)");

                ui = UI.getCurrent();

                new Thread(() -> {

                    synchronized (lock) {
                        try {
                            System.out.println("Esperando clic en el mapa...");
                            lock.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Error esperando clic en el mapa"+e);
                            return;
                        }
                    }
                    if (ui != null) {
                        ui.access(() -> this.mapContainer.removeClassName("map_action"));
                    }
                }).start();

                break;
            case ZONA:
                this.mapContainer.addClassName("map_action");
                if (!this.controller.isZone()){
                    this.controller.createDialogZona();
                    System.out.println("Registrando puntos para la zona");
                    map.on("click", clickFuncReference);
                    button.setEnabled(false);
                    button.setText("Terminar zona");
                    this.controller.setZone(true);
                }else {
                    this.map.off("click", clickFuncReference);
                    System.out.println("Zona terminada");
                    button.setText("Zona");
                    button.setEnabled(true);
                    this.controller.createZone(this.zoneMarkerPoints);
                    this.controller.setZone(false);
                    this.mapContainer.removeClassName("map_action");

                    for (LMarker marker : zoneMarkers.values()){
                        marker.removeFrom(this.map);
                    }

                    zoneMarkers.clear();
                    zoneMarkerPoints.clear();
                }
                break;
        }
    }

    @ClientCallable
    public void mapTarea(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        controller.createNeed(obj.getNumber("lat"), obj.getNumber("lng"));

        synchronized (lock) {
            lock.notify();
        }
        ui.push();
    }

    @ClientCallable
    public void mapZona(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        LMarker marker = this.controller.createZoneMarker(obj.getNumber("lat"), obj.getNumber("lng"));

        zoneMarkerPoints.add(new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng")));
        zoneMarkers.put(new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng")), marker);

        if (zoneMarkerPoints.size() > 2){
            zona.setEnabled(true);
        }
    }

    @ClientCallable
    public void zoneMarkerStart(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        this.zoneMarkerStartingPoint = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));
    }

    @ClientCallable
    public void zoneMarkerEnd(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        Tuple<Double, Double> point = new Tuple<>(obj.getNumber("lat"), obj.getNumber("lng"));

        int index = -1;
        for (int i = 0; i < zoneMarkerPoints.size(); i++) {
            Tuple<Double, Double> t = zoneMarkerPoints.get(i);

            if (t._1().equals(this.zoneMarkerStartingPoint._1()) && t._2().equals(this.zoneMarkerStartingPoint._2())) {
                index = i;
                break;
            }
        }

        System.out.println(this.zoneMarkerStartingPoint);
        zoneMarkerPoints.set(index, point);
    }


}