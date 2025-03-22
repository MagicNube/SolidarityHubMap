package es.pingu.map.views;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import es.pingu.map.commons.NavigationBar;
import es.pingu.map.controllers.MapControllTypes;
import es.pingu.map.controllers.MapController;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.layer.raster.LTileLayer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.map.LMapLocateOptions;
import es.pingu.map.commons.NavigationBar;
import es.pingu.map.controllers.MapController;
import software.xdev.vaadin.maps.leaflet.MapContainer;
import software.xdev.vaadin.maps.leaflet.map.LMap;
import software.xdev.vaadin.maps.leaflet.registry.LComponentManagementRegistry;
import software.xdev.vaadin.maps.leaflet.registry.LDefaultComponentManagementRegistry;

@Route("map")
@PageTitle("Visor del mapa")
public class MapView extends HorizontalLayout {

    MapController controller;
    private final String ID = "mapa";
    private final LMap map;
    private final Object lock = new Object();
    private UI ui;
    private final Button tarea = new Button("Tarea");
    private final Button zona = new Button("Zona");

    public MapView() {
        this.setId(ID);
        this.setSizeFull();
        this.add(NavigationBar.createNavBar());

        VerticalLayout MapVerticalLayout = new VerticalLayout();
        HorizontalLayout ButtonLayout = new HorizontalLayout();

        this.add(MapVerticalLayout);

        final LComponentManagementRegistry reg = new LDefaultComponentManagementRegistry(this);
        final MapContainer mapContainer = new MapContainer(reg);
        mapContainer.setSizeFull();
        this.map = mapContainer.getlMap();
        this.map.addLayer(LTileLayer.createDefaultForOpenStreetMapTileServer(reg));
        this.map.locate(new LMapLocateOptions().withSetView(true));

        MapVerticalLayout.add(mapContainer);
        MapVerticalLayout.add(ButtonLayout);
        tarea.isDisableOnClick();
        ButtonLayout.add(tarea, zona);


        controller = new MapController(reg, map);

        tarea.addClickListener(e -> click(MapControllTypes.TAREA, tarea));
        zona.addClickListener(e -> click(MapControllTypes.ZONA, zona));

    }


    public void click(MapControllTypes Action, Button button) {
        switch (Action) {
            case TAREA:
                this.map.once("click", "e => document.getElementById('" + ID + "').$server.mapTarea(e.latlng)");

                ui = UI.getCurrent();

                new Thread(() -> {

                    synchronized (lock) {
                        try {
                            System.out.println("Esperando clic en el mapa...");
                            lock.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Erros esperando clic en el mapa"+e);
                            return;
                        }
                    }
                    if (ui != null) {
                        ui.access(controller::createTask);
                    }
                }).start();

                break;
            case ZONA:
                if (!controller.isZone()){
                    System.out.println("Registrando puntos para la zona");
                    this.map.on("click", "e => document.getElementById('" + ID + "').$server.mapZona(e.latlng)");
                    button.setEnabled(false);
                    button.setText("Terminar zona");
                    controller.setZone(true);
                }else {
                    this.map.off();
                    button.setText("Zona");
                    button.setEnabled(true);
                    controller.createZone();
                    controller.setZone(false);
                }
                break;
        }


    }

    @ClientCallable
    public void mapTarea(final JsonValue input) {
        if (!(input instanceof final JsonObject obj)) {
            return;
        }

        controller.setCoords(obj.getNumber("lat"), obj.getNumber("lng"));

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

        controller.addPoint(obj.getNumber("lat"), obj.getNumber("lng"));

        if (controller.getNumPoints() > 2){
            zona.setEnabled(true);
        }

    }
}
